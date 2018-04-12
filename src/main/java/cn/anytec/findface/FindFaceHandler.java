package cn.anytec.findface;


import cn.anytec.config.AppConfig;
import cn.anytec.mongo.MongoHandler;
import cn.anytec.util.WsMessStore;
import com.mongodb.util.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


@Component
public class FindFaceHandler {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private MongoHandler mongoHandler;


    private static Logger logger = LoggerFactory.getLogger(FindFaceHandler.class);
    private static JSONParser jsonParser = new JSONParser();
//    private static Base64.Encoder encoder = Base64.getEncoder();


    public JSONObject imageIdentify(Map<String,String[]> params, byte[] pic){
        logger.info("===========identify============");
        ByteArrayOutputStream out = null;
        HttpResponse response;
        HttpEntity entity;
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().addBinaryBody("photo",pic, ContentType.DEFAULT_BINARY, "photo");

        String camera=params.get("camera")[0];
        String token=params.get("token")[0];
        String Lat ="";
        String Lng ="";

        if(params.containsKey("threshold")){
            multipartEntityBuilder.addTextBody("threshold",params.get("threshold")[0]);
        }

        if(params.containsKey("Lat")){
           Lat=params.get("Lat")[0];
        }

        if(params.containsKey("Lng")){
           Lng = params.get("Lng")[0];
        }

//        if(params.containsKey("mf_selector"))
        multipartEntityBuilder.addTextBody("mf_selector", "all");
        if (params.containsKey("n")) {
            multipartEntityBuilder.addTextBody("n", params.get("n")[0]);
        } else {
            multipartEntityBuilder.addTextBody("n", "1");
        }
        if (params.containsKey("bbox"))
            multipartEntityBuilder.addTextBody("bbox", params.get("bbox")[0]);
        entity = multipartEntityBuilder.build();
        try {
            response = Request.Post(appConfig.getSDKURI() + "/v0/identify")
                    .connectTimeout(10000)
                    .socketTimeout(30000)
                    .addHeader("Authorization", "Token " + token)
                    .body(entity)
                    .execute().returnResponse();
            String reply = EntityUtils.toString(response.getEntity());

            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                logger.info(reply);
                JSONObject replyJson = new JSONObject();
//                replyJson.put("photo",encoder.encodeToString(pic));
                JSONArray resultValue = new JSONArray();
//                JSONArray saveValue = new JSONArray();
                JSONObject root = (JSONObject) jsonParser.parse(reply);
                if (root.containsKey("results")) {
                    Image srcImg = Toolkit.getDefaultToolkit().createImage(pic);
                    JSONObject results = (JSONObject) root.get("results");
                    Iterator iterator = results.keySet().iterator();
                    while (iterator.hasNext()){
                        String faceCoordinates = (String)iterator.next();
                        JSONArray jsonArray = (JSONArray) results.get(faceCoordinates);
                        if (jsonArray.size() == 0) {
//                            resultValue.add(new JSONObject().put(faceCoordinates,"no match"));
                            continue;
                        }
                        //==========根据坐标切出人脸图片=============
                        String cutFacePicId = String.valueOf(new Date().getTime());
                        String normalCoordinates = faceCoordinates.replace("[", "");
                        normalCoordinates = normalCoordinates.replace("]", "");
                        normalCoordinates = normalCoordinates.replace(" ", "");
                        String[] faceCoordiante = normalCoordinates.split(",");
                        int x1 = Integer.valueOf(faceCoordiante[0]);
                        int y1 = Integer.valueOf(faceCoordiante[1]);
                        int x2 = Integer.valueOf(faceCoordiante[2]);
                        int y2 = Integer.valueOf(faceCoordiante[3]);
                        int width = x2 - x1;
                        int height = y2 - y1;
                        if (x1 < 0) {
                            width = width + x1;
                            x1 = 0;
                        }
                        if (y1 < 0) {
                            height = height + y1;
                            y1 = 0;
                        }

                        //根据sdk返回坐标切出人脸并保存文件到配置的目录。
                        BufferedImage bufferedImage = cutImage(srcImg, width, height, x1, y1);
                        out = new ByteArrayOutputStream();
                        ImageIO.write(bufferedImage, "jpeg", out);
//                        String base64Img = encoder.encodeToString(out.toByteArray());
                        String cutFacePathAndName=appConfig.getCutFace()+camera+"/"+cutFacePicId+""+".jpg";
                        File cutFace = createFile(cutFacePathAndName);
                        ImageIO.write(bufferedImage, "jpg", cutFace);
                        out.close();

                        //根据sdk返回坐标画出人脸并保存文件到配置的文件
                        String drawFacePicId = String.valueOf(new Date().getTime());
                        BufferedImage drawboxImage = drawBox(srcImg, width, height, x1, y1);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(drawboxImage, "jpeg", baos);
//                        String drawboxImage64Img = encoder.encodeToString(baos.toByteArray());
                        String drawFacePathAndName=appConfig.getDrawFace()+camera+"/"+drawFacePicId+""+".jpg";
                        File tmpFile = createFile(drawFacePathAndName);
                        ImageIO.write(drawboxImage, "jpg", tmpFile);
                        baos.close();


                        JSONObject match = (JSONObject) jsonArray.get(0);
                        JSONObject face = (JSONObject) match.get("face");

                        Map<String,Object> faceInfo = new HashMap<>();
                        faceInfo.put("coordinate",faceCoordinates);
                        faceInfo.put("confidence",match.get("confidence"));
                        faceInfo.put("id",face.get("id"));
                        faceInfo.put("meta",face.get("meta"));
                        faceInfo.put("friend",face.get("friend"));
                        faceInfo.put("timestamp",new Date().getTime());
                        faceInfo.put("face",appConfig.getURI()+cutFacePathAndName);
//                        faceInfo.put("photo","http://u1961b1648.51mypc.cn:13319/static/resource/"+camera+"/"+picId+""+String.valueOf(i)+".jpg");
                        faceInfo.put("photo", appConfig.getURI() + drawFacePathAndName);
//                        faceInfo.put("photo",drawboxImage64Img);
//                        faceInfo.put("matchFace",face.get("normalized"));

                        faceInfo.put("matchFace",face.get("normalized").toString().replaceFirst("192.168.1.138:3333","u1961b1648.51mypc.cn:23887"));
                        faceInfo.put("camera",camera);
                        faceInfo.put("latitude",Lat);
                        faceInfo.put("longitude",Lng);
                        resultValue.add(new JSONObject(faceInfo));

//                        String photoUrl="http://192.168.10.212:8090/static/resource/"+camera+"/"+picId+""+String.valueOf(i)+".jpg";
//                        faceInfo.put("photo",photoUrl);
//                        saveValue.add(faceInfo);

                    }

                    replyJson.put("results", resultValue);
                    //向前台推送消息
//                    String uri=appConfig.getURI()+"/pushToClient";
//                    Request.Post(uri)
//                            .connectTimeout(10000)
//                            .socketTimeout(30000)
////                            .addHeader("Authorization", "Token " + sdkConfig.())
//                            .body(MultipartEntityBuilder
//                                    .create()
//                                    .setCharset(Charset.forName(HTTP.UTF_8))
//                                    //.addTextBody("mf_selector", "all")
//                                    //                        .addTextBody("n", "1")
//                                    //                        .addTextBody("bbox", String.format("[[%s,%s,%s,%s]]", x1, y1, x2, y2))
//                                    .addTextBody("pushJson",replyJson.toJSONString())
////                                    .addBinaryBody("photo2", photo2.getBytes(),ContentType.DEFAULT_BINARY,"photo2")
//                                    .build())
//                            .execute().returnResponse();

                    //全局监控推送
                    WsMessStore.getInstance().addMessage(replyJson.toJSONString());
                    //图片信息入库
                    params.keySet().stream().forEach((key)->{
                        if(!key.equals("camera")&&!key.equals("token")&&!key.equals("threshold"))
                            replyJson.put(key,params.get(key)[0]);
                    });
                    mongoHandler.notifyMongo(replyJson);
                    return replyJson;

                }
            } else {
                logger.info("请求未正确响应：" + responseCode);
                logger.info(reply);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private BufferedImage cutImage(Image image, int width, int height, int x, int y) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(width,
                    height, transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(width,
                    height, type);
        }
        // Copy image to buffered image
        Graphics g = bimage.getGraphics();
        // Paint the image onto the buffered image

        g.drawImage(image, -x, -y, null);
        g.dispose();
        return bimage;
    }

    private BufferedImage drawBox(Image image, int width, int height, int x, int y) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        // Create a buffered image using the default color model
        int type = BufferedImage.TYPE_INT_RGB;
        bimage = new BufferedImage(image.getWidth(null),
                image.getHeight(null), type);
        // Copy image to buffered image
        Graphics g = bimage.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        // Paint the image onto the buffered image
        g2d.drawImage(image, 0, 0, null);
        Stroke stroke = new BasicStroke(4.0f);//设置线宽为3.0
        g2d.setStroke(stroke);
        g2d.setColor(Color.green);//画笔颜色
        g2d.drawRect(x, y, width, height);
        g2d.dispose();
        return bimage;
    }

    public static File createFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            return new File(destFileName);
        }
        if (destFileName.endsWith(File.separator)) {
            logger.error("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
            return null;
        }
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            logger.debug("目标文件所在目录不存在，准备创建它！");
            if(!file.getParentFile().mkdirs()) {
                logger.error("创建目标文件所在目录失败！");
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {
                logger.debug("创建单个文件" + destFileName + "成功！");
                return new File(destFileName);
            } else {
                logger.error("创建单个文件" + destFileName + "失败！");
                return null;
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("创建单个文件" + destFileName + "失败！" + e.getMessage());
//            return false;
        }
        return null;
    }


//    public static boolean createDir(String destDirName) {
//        File dir = new File(destDirName);
//        if (dir.exists()) {
//            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
//            return false;
//        }
//        if (!destDirName.endsWith(File.separator)) {
//            destDirName = destDirName + File.separator;
//        }
//        //创建目录
//        if (dir.mkdirs()) {
//            System.out.println("创建目录" + destDirName + "成功！");
//            return true;
//        } else {
//            System.out.println("创建目录" + destDirName + "失败！");
//            return false;
//        }
//    }

//    private String drawFaceBox(Image image,int width,int height,int x1 ,int y1) {
//        ByteArrayOutputStream out=null;
//        try {
//
//            if (image instanceof BufferedImage) {
//                return "";
//            }
//            // This code ensures that all the pixels in the image are loaded
//            image = new ImageIcon(image).getImage();
//            BufferedImage bimage = null;
//            // Create a buffered image using the default color model
//            int type = BufferedImage.TYPE_INT_RGB;
//            bimage = new BufferedImage(image.getWidth(null),
//                    image.getHeight(null), type);
////            image = ImageIO.read(in);
//            Graphics g2d = bimage.getGraphics();
////            Graphics2D g2d=(Graphics2D)g;
//            g2d.setColor(Color.RED);//画笔颜色
//            Stroke stroke=new BasicStroke(4.0f);//设置线宽为3.0
////            g2d.setStroke(stroke);
//            g2d.drawRect(x1, y1, width, height);
//            g2d.dispose();
//            out= new ByteArrayOutputStream();
//            ImageIO.write(bimage, "jpeg", out);
//            byte[] img = out.toByteArray();
////            out.close();
//            return encoder.encodeToString(img);
//        } catch (IOException e) {
//            logger.error("io exception " + e.getMessage());
//            return "";
//        }finally {
//            try {
//                out.close();
//            } catch (IOException e) {
//                logger.error(e.getMessage());
//            }
//        }

//    }

    public List<JSONObject> getPhotoResults(List<JSONObject> photoList){
        List<JSONObject> resultList = new ArrayList<>();
        List<JSONObject> dayList = new ArrayList<>();
        JSONObject timeJo = (JSONObject)photoList.get(0).get("timestamp");
        Long timeValue =Long.parseLong(timeJo.get("$numberLong").toString());
        Calendar cld = Calendar.getInstance();
        cld.setTime(new Date(timeValue));
        Integer year = cld.get(Calendar.YEAR);
        Integer month = (cld.get(Calendar.MONTH)+1);
        Integer day = cld.get(Calendar.DATE);
        for(int i=0;i<photoList.size();i++){
            JSONObject indexObj = photoList.get(i);
            JSONObject indexTimeObject = (JSONObject)indexObj.get("timestamp");
            cld.setTime(new Date(Long.parseLong(indexTimeObject.get("$numberLong").toString())));
            Integer indexYear = cld.get(Calendar.YEAR);
            Integer indexMonth = (cld.get(Calendar.MONTH)+1);
            Integer indexDay = cld.get(Calendar.DATE);
            Integer indexH = cld.get(Calendar.HOUR);
            Integer indexM = cld.get(Calendar.MINUTE);
            Integer indexS = cld.get(Calendar.SECOND);
            String indexTime = indexH+ ":"+ indexM + ":"+indexS;
            indexObj.put("time",indexTime);
            indexObj.put("year",indexYear);
            indexObj.put("month",indexMonth);
            indexObj.put("day",indexDay);
            if (indexYear.equals(year)) {
                if (indexMonth.equals(month)) {
                    if (indexDay.equals(day)) {
                        dayList.add(indexObj);
                    } else {
                        JSONObject dayJo = new JSONObject();
                        List<JSONObject> list = new ArrayList<>();
                        list.addAll(dayList);
                        dayJo.put(year+"年"+month+"月"+day,list);
                        resultList.add(dayJo);
                        day = indexDay;
                        dayList.clear();
                        dayList.add(indexObj);
                    }
                } else {
                    JSONObject dayJo = new JSONObject();
                    List<JSONObject> list = new ArrayList<>();
                    list.addAll(dayList);
                    dayJo.put(year+"年"+month+"月"+day,list);
                    resultList.add(dayJo);
                    day = indexDay;
                    month = indexMonth;
                    dayList.clear();
                    dayList.add(indexObj);
                }
            } else {
                JSONObject dayJo = new JSONObject();
                List<JSONObject> list = new ArrayList<>();
                list.addAll(dayList);
                dayJo.put(year+"年"+month+"月"+day,list);
                resultList.add(dayJo);
                day = indexDay;
                month = indexMonth;
                dayList.clear();
                dayList.add(indexObj);
                year = indexYear;
            }
            if(i==photoList.size()-1){
                JSONObject dayJo = new JSONObject();
                List<JSONObject> list = new ArrayList<>();
                list.addAll(dayList);
                dayJo.put(year+"年"+month+"月"+day,list);
                resultList.add(dayJo);
            }
        }
        return resultList;
    }



//    public JSONObject getPhotoResult(List<JSONObject> reusltList) {
//        JSONObject mjo = new JSONObject();
//        JSONObject yjo = new JSONObject();
//        JSONObject jo = new JSONObject();
//        Map<String,Object> timeMap = new HashMap<>();
//        Calendar cld = Calendar.getInstance();
//        cld.setTime((Date)reusltList.get(0).get("date"));
//        Integer year = cld.get(Calendar.YEAR);
//        Integer month = (cld.get(Calendar.MONTH)+1);
//        Integer day = cld.get(Calendar.DATE);
//        for(int i=0;i<reusltList.size();i++){
//            cld.setTime((Date)reusltList.get(i).get("date"));
//            Integer indexYear = cld.get(Calendar.YEAR);
//            Integer indexMonth = (cld.get(Calendar.MONTH)+1);
//            Integer indexDay = cld.get(Calendar.DATE);
//            Integer indexH = cld.get(Calendar.HOUR);
//            Integer indexM = cld.get(Calendar.MINUTE);
//            Integer indexS = cld.get(Calendar.SECOND);
//            String indexTime = indexH+ ":"+ indexM + ":"+indexS;
//            if (indexYear.equals(year)) {
//                if (indexMonth.equals(month)) {
//                    if (indexDay.equals(day)) {
//                        timeMap.put("time",indexTime);
//                        timeMap.put("photo",reusltList.get(i).get("photo"));
//                    } else {
//                        mjo.put(day,timeMap);
//                        timeMap.clear();
//                        timeMap.put("time",indexTime);
//                        timeMap.put("photo",reusltList.get(i).get("photo"));
//                        day = indexDay;
//                    }
//                } else {
//                    mjo.put(day,timeMap);
//                    yjo.put(month,mjo);
//                    timeMap.clear();
//                    mjo.clear();
//                    timeMap.put("time",indexTime);
//                    timeMap.put("photo",reusltList.get(i).get("photo"));
//                    day = indexDay;
//                    month = indexMonth;
//                }
//            } else {
//                mjo.put(day,timeMap);
//                yjo.put(month,mjo);
//                jo.put(year,yjo);
//                timeMap.clear();
//                mjo.clear();
//                yjo.clear();
//                timeMap.put("time",indexTime);
//                timeMap.put("photo",reusltList.get(i).get("photo"));
//                day = indexDay;
//                month = indexMonth;
//                year = indexYear;
//            }
//            if(i==reusltList.size()-1){
//                mjo.put(day,timeMap);
//                yjo.put(month,mjo);
//                jo.put(year,yjo);
//            }
//        }
//        return jo;
//    }
}
