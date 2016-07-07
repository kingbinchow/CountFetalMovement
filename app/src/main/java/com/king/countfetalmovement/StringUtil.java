package com.king.countfetalmovement;

import android.content.Context;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lucy.chen on 2016/1/7.
 */
public class StringUtil {
    /**
     * 判断字串是否为空
     * @param str
     * @return
     */
    public static boolean emptyOrNull(String str) {
        return str == null || str.length() == 0;
    }

    /*
  判断手机号
   */
    public static boolean adjustPhoneNumError(String number) {
        String regExp = "^[1][0-9]{10}$";
        return !number.matches(regExp);
        //Pattern p = Pattern.compile(regExp);
        //Matcher m = p.matcher(number);
        //return m.find();
    }

    public static  boolean  isPhoneNum(String number){
      //  String regExp ="^((13[0-9])|(15[^4,\\D])|(18[0-1,5-9]))\\d{8}$";
        String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[6-8]|(14[5,7])))\\d{8}$";
        return !number.matches(regExp);
    }

    /**
     * email是否合法
     *
     * @param mail
     * @return true or false
     */
    public static boolean isValidEmail(String mail) {
        Pattern pattern = Pattern
                .compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
        Matcher mc = pattern.matcher(mail);
        return mc.matches();
    }

    public static String getEncodeStr(String code,boolean isIdentity){
        StringBuffer encryptUserName = new StringBuffer("");
        if(!StringUtil.emptyOrNull(code)){
            if(isIdentity){
                if(code.length()>14){
                    //身份证
                    encryptUserName.append(code.substring(0,3)).append("***********").append(code.substring(code.length()-3,code.length()));
                }
            }else{

                if(code.length()>5){
                    encryptUserName.append(code.substring(0,3)).append("****").append(code.substring(code.length()-4,code.length()));
                }else if(code.length()<6 && code.length()>0){
                    encryptUserName.append("**").append(code.substring(code.length()-1,code.length()));
                }
            }
        }
        return encryptUserName.toString();
    }

    //手机号等字符串打码
    public static String getUserNameEncrypt(String name){
        StringBuffer encryptUserName = new StringBuffer("");
        if(StringUtil.isValidEmail(name)){
            //@符号签名后面显示
            int index = name.indexOf("@");
            int length = name.length();
            if(index>8){
                encryptUserName.append(name.substring(0,2)).append("*****").append(name.substring(index-2,index));
            }else if(index==8){
                encryptUserName.append(name.substring(0,2)).append("****").append(name.substring(index-2,index));
            }else if(index==7){
                encryptUserName.append(name.substring(0,2)).append("***").append(name.substring(index-2,index));
            }else if(index==6){
                encryptUserName.append(name.substring(0,2)).append("**").append(name.substring(index-2,index));
            }else if(index == 5){
                encryptUserName.append(name.substring(0,2)).append("*").append(name.substring(index-2,index));
            }else if(index == 4){
                encryptUserName.append(name.substring(0,1)).append("**").append(name.substring(index-1,index));
            }else if(index == 3){
                encryptUserName.append(name.substring(0,1)).append("*").append(name.substring(index-1,index));
            }else if(index == 2){
                encryptUserName.append(name.substring(0,1)).append("*");
            }else if(index == 1){
                encryptUserName.append("*");
            }
            encryptUserName.append(name.substring(index,length));
        }else{
            //常规字符串先前后各3位，不足 <6 则前后各2位，中间加*
            if(!StringUtil.emptyOrNull(name)){
                int strLength = name.length();
                if(strLength>6){
                    encryptUserName.append(name.substring(0,3));
                    int sunEnd = strLength-4;//后面开始截取的位数
                    int diff = sunEnd - 3;
                    if(diff>5){
                        diff = 5;//最长5个
                    }
                    for(int i =0;i<diff;i++){
                        encryptUserName.append("*");
                    }
                    encryptUserName.append(name.substring(strLength - 4, strLength));
                }else if(strLength ==1) {
                    encryptUserName.append("*");
                }else if(strLength == 2){
                    encryptUserName.append(name.substring(0,1)).append("*");
                }else if(strLength == 3){
                    encryptUserName.append(name.substring(0,1)).append("*").append(name.substring(strLength-1,strLength));;
                }else if(strLength == 4){
                    encryptUserName.append(name.substring(0,1)).append("**").append(name.substring(strLength-1,strLength));;
                }else if(strLength==5){
                    encryptUserName.append(name.substring(0,2)).append("*").append(name.substring(strLength-2,strLength));
                }else if(strLength==6){
                    encryptUserName.append(name.substring(0,2)).append("**").append(name.substring(strLength-2,strLength));
                }
            }
        }
        Log.d("encry", encryptUserName.toString());
        return  encryptUserName.toString();
    }

    public static boolean isNull(Object obj) {
        if (null == obj || obj == "" || obj.equals("")) {
            return true;
        }
        return false;
    }

    public static int dip2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String getBankLastNum(String bankNum){
        String lastBankNum="";
        lastBankNum=bankNum.substring(bankNum.length()-4,bankNum.length());
        return lastBankNum;

    }

}
