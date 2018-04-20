#!/bin/bash
set -x
#fetch pictures from folder and send request to server

#backup_path="/backup"
#current_path=$(pwd)
#pic_path="${current_path}/$1"
#pic_path="$1"
token=$(findface-facenapi.token)
#api="http://127.0.0.1:8000/v0/detect"
#api="http://192.168.10.212:8090/anytec/identify"
config_path=/anytec/server/southOffice
read_parameter(){
        backup_path=`cat $config_path/config.ini | grep backup_path| awk -F'=' '{ print $2 }' | sed s/[[:space:]]//g`
        if [ -z $backup_path ];then
                echo "backup_file is null ,please configure it"
                exit 1
        fi
        pic_path=`cat $config_path/config.ini | grep pic_path| awk -F'=' '{ print $2 }' | sed s/[[:space:]]//g`
        if [ -z $pic_path ];then
                echo "picture path is null ,please configure it"
                exit 1
        fi
        api=`cat $config_path/config.ini | grep api| awk -F'=' '{ print $2 }' | sed s/[[:space:]]//g`
        if [ -z $api ];then
                echo "picture path is null ,please configure it"
                exit 1
        fi
        tomcat_log=`cat $config_path/config.ini | grep tomcat_log| awk -F'=' '{ print $2 }' | sed s/[[:space:]]//g`
        if [ -z $tomcat_log ];then
                echo "tomcat_log path is null ,please configure it"
                exit 1
        fi
        threshold=`cat $config_path/config.ini | grep threshold| awk -F'=' '{ print $2 }' | sed s/[[:space:]]//g`
        if [ -z $threshold ];then
                echo "threshold path is null ,please configure it"
                exit 1
        fi
        script_path=`cat $config_path/config.ini | grep script_path| awk -F'=' '{ print $2 }' | sed s/[[:space:]]//g`
        if [ -z $script_path ];then
                echo "script_path path is null ,please configure it"
                exit 1
        fi

#       script_log=`cat $config_path/config.ini | grep script_log| awk -F'=' '{ print $2 }' | sed s/[[:space:]]//g`
#       if [ -z $script_log ];then
#               echo "script_log path is null ,please configure it"
#               exit 1
#       fi
}

check_trans_finished(){
    if [ "X$1" = "X" ];then
        echo "empty parameter"
        return 2
    fi
    #local flag=1
    local file_size=$(cksum $1|awk -F' ' '{print $1}')
    while ((1))
    do
        local tmp_size=$(cksum $1|awk -F' ' '{print $1}')
        if [  "$tmp_size" = "$file_size" ];then
            return 0
           #flag=0; 
        fi
        file_size=$(cksum $1|awk -F' ' '{print $1}')
    done
}

processing_folder(){
        #       echo "processing folder $1"

        #判断参数是不是文件夹,不是则提示退出
        if [ ! -d $1 ];then
                echo "$1 is not a folder"
                exit 1
        fi

        #参数是文件夹,遍历文件夹中的内容
        for camera in $(ls $1)
        do
                #判断摄像头文件夹目录下的文件是不是文件夹,不是则直接移到备份目录
                if [ ! -d $pic_path/$camera ];then
                        echo " $pic_path/$camera in $1 is not a folder"
                        mv $pic_path/$camera $backup_path
                        continue
                fi
                #如果camera目录是文件夹,则在备份目录创建备份文件夹
                if [ ! -d $backup_path/$camera ];then
                        #rm $backup_path/$camera
                        mkdir -p $backup_path/$camera
                fi

                #是文件夹,则遍历该该摄像头下的日期文件目录
                for date in $(ls $pic_path/$camera)
                do
                        abs_date=$pic_path/$camera/$date
                        #判断日期目录下的文件是不是文件夹,不是则直接移到备份目录
                        if [ ! -d $abs_date ];then
                                echo "$abs_date in $pic_path/$camera is not a folder"
                                mv $abs_date $backup_path/$camera
                                continue
                        fi

                        #如果日期目录是文件夹,则在备份目录创建对应摄像头目录下创建日期的备份文件夹
                        backup_date=$backup_path/$camera/$date
                        if [ ! -d $backup_date ];then
                                mkdir -p $backup_date
                        fi
                        #遍历该日期目录
                        for picture in $(ls $abs_date)
                        do
                                abs_picture=$abs_date/$picture
                                #判断文件夹内的文件后缀名,只处理jpg|jpeg|png结尾的图片
                                suffix=$(echo $picture|awk -F. '{print $NF}')
                                if [ "X${suffix}" = "Xjpg" -o "X${suffix}" = "Xjpeg" -o "X${suffix}" = "Xpng" ];then
                                        #echo $suffix|grep -ie "jpg|jpeg|png"
                                        #if [ $? -eq 0 ];then
                                        #sleep 1
                                        #sudo lsof +D $pic_path/$camera|grep $picture
                                        check_trans_finished $abs_picture
                                        #local check_status=$?
                                        #if [ $check_status -ne 0 ];then
                                        #  continue  
                                        #fi
                                        if [ $? -eq 0 ];then
                                          #curl -F "photo=@/$abs_picture" -F "token=${token}" -F "camera=${camera}" -F "threshold=${threshold}" ${api}

                                          /usr/bin/python3 ${script_path}/split_data.py  ${abs_picture}  ${token} ${camera} ${threshold} ${api}
                                          echo "end handling pic $abs_picture"
                                          mv $abs_picture $backup_date
                                        else
                                          echo "pic $abs_picture in transmission"
                                        fi
                                else
                                        echo "$file_abs is not a picture with suffix jpg|jpeg|png"
                                        mv $abs_picture $backup_date
                                fi
                                #file_path=${pic_path}/${file}
                        done
                done
        done
}
#if [ $# -ne 1 ];then
#       echo "please input one parameter as folder"
#       exit 1
#fi
read_parameter
#判断application是否在运行,没有则启动,有则重启
#ps -ef|grep -v grep|grep southOffice-1.0-SNAPSHOT.jar
#if [ $? -ne 0 ];then
#        echo "server process not in running ,prepare to start it"
#        if [ -f /anytec/server/southOffice/lib/southOffice-1.0-SNAPSHOT.jar ];then
#                echo "run jar exist ,begin to start server"
#                nohup /usr/lib/jvm/jdk1.8.0_144/bin/java        -jar /anytec/server/southOffice/lib/southOffice-1.0-SNAPSHOT.jar 2>&1 >> $tomcat_log &
#        else
#                echo "application jar not exit in /anytec/server/southOffice/lib/ ,please check it "
#                exit 1
#        fi
#else
#        echo "application jar is in running ,now restart it"
#        APP_PID=$(pgrep -f southOffice-1.0-SNAPSHOT.jar)
#        if [ -n $APP_PID ];then
#                echo "begin to restart server"
#                kill -9 $APP_PID
#                if [ -f /anytec/server/southOffice/lib/southOffice-1.0-SNAPSHOT.jar ];then
#                        echo "run jar exist ,begin to start server"
#                        nohup /usr/lib/jvm/jdk1.8.0_144/bin/java  -jar /anytec/server/southOffice/lib/southOffice-1.0-SNAPSHOT.jar 2>&1 >> $tomcat_log &
#                else
#                        echo "application jar not exit in /anytec/server/southOffice/lib/ ,please check it "
#                        exit 1
#                fi
#        fi
#fi
#echo "server has started"
old_IFS=$IFS
IFS=$(echo -en "\n\b")
while ((1))
do
        processing_folder $pic_path
        sleep 1
done
#用完恢复老的IFS
IFS=$old_IFS
              
