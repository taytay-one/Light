package com.example.Light.BtThread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.Light.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Locale;

public class ConnectedThread extends Thread{
    BluetoothSocket bluetoothSocket=null;
    InputStream inputStream=null;//获取输入数据
    OutputStream outputStream=null;//获取输出数据
    int lastData=0;
    protected final static String mHexStr = "0123456789ABCDEF";//检查是否为16进制数
    public ConnectedThread(BluetoothSocket bluetoothSocket){
        this.bluetoothSocket=bluetoothSocket;
        //先新建暂时的Stream
        InputStream inputTemp=null;
        OutputStream outputTemp=null;
        try {
            inputTemp=this.bluetoothSocket.getInputStream();
            outputTemp=this.bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            try {
                bluetoothSocket.close();//出错就关闭线程吧
            } catch (IOException ex) {}
        }
        inputStream=inputTemp;
        outputStream=outputTemp;
    }

    @Override
    public void run() {
        super.run();
        while(true){
            if(MainActivity.nowData != lastData){
                btWriteHexString(String.valueOf(MainActivity.nowData));
                lastData=MainActivity.nowData;//做完一次发送重新给lastData赋值
            }
        }
    }

    public void btWriteInt(int[] intData){
        for(int sendInt:intData){
            try {
                outputStream.write(sendInt);
            } catch (IOException e) {}
        }
    }

    //自定义的发送字符串的函数
    public void btWriteHexString(String string){
        byte[] sendData = null;
        Log.i("ConnectedThread", "sendData");
        if (checkHexStr(string)) {
           sendData = hexStringToBytes(string);
            Log.i("btWriteString", "输出字符数组: " + Arrays.toString(sendData));
            try {
                outputStream.write(sendData);//outputStream发送字节的函数
            } catch (IOException e) {}
        }
    }

    public static boolean checkHexStr(String sHex) {
        String sTmp = sHex.toString().trim().replace(" ", "").toUpperCase(Locale.US);
        int iLen = sTmp.length();

        if (iLen > 1 && iLen % 2 == 0) {
            for (int i = 0; i < iLen; i++)
                if (!mHexStr.contains(sTmp.substring(i, i + 1)))
                    return false;
            return true;
        } else
            return false;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) mHexStr.indexOf(c);
    }

    //自定义的关闭Socket线程的函数
    public void cancel(){
        try {
            bluetoothSocket.close();
        } catch (IOException e) {}
    }
}
