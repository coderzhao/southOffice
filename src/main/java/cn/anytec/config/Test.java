package cn.anytec.config;

public class Test {
    public static void main(String[] args) {
        String abc="http://u1961b1648.51mypc.cn:9090";
        String abcd="http://192.168.10.212:3333/uploads/5a26735efc7919315636ab36/20180120/3882070516415551_45898319e754_norm.png";
        System.out.println(abcd.replaceFirst("192.168.10.212:3333","u1961b1648.51mypc.cn:23887"));
    }
}
