package com.ble.demo.viewmodel;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.ble.demo.MainApp;
import com.ble.mylibrary.interfaces.BatteryStatusCallback;
import com.ble.mylibrary.interfaces.PeelMode;
import com.ble.mylibrary.interfaces.WeightMode;
import com.ble.mylibrary.interfaces.WeightUpdateCallback;
import com.ble.mylibrary.outdevice.LScaleHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Presenter {

    /**
     * 搜索
     */
    public void search(Context context) {
        boolean isAutoConnect = false; //是否自动连接
        List<BluetoothDevice> list =  new ArrayList<>(); //搜索到的设备
        LScaleHelper.getInstance(context).startscan(isAutoConnect, (device, rssi) -> {
            if (!list.contains(device)){
                list.add(device);
            }
        });
    }

    /**
     * 连接
     *
     * @param device:要连接的设备
     */
    public void connect(Context context, BluetoothDevice device) {
        LScaleHelper.getInstance(context).connect(device, (result, device1) -> {
            /**result:
             * 1:连接成功
             * 2:连接失败
             * 3:连接断开
             */
            if (result == 1) {
                String device_name = device1.getName();
                String device_address = device1.getAddress();
            }


        });
    }

    /**
     * 获取数据
     * weight:重量(公斤)
     * isStable:是否稳定
     * isTared:是否有皮重
     * isZero:是否是零点
     * PeelMode:皮重模式----
     * dataStatus:数据标志
     * error:是否有错误
     * rssi:信号强度(负数)-----0至-100,越大信号越强
     */
    public void getData(Context context) {
        LScaleHelper.getInstance(context).setWUCallback(new WeightUpdateCallback() {
            @Override
            public void onWeightUpdate(BigDecimal weight, boolean isStable, boolean isTared, boolean isZero, PeelMode peelMode, int dataStatus, boolean error) {
                if (error) {
                    switch (dataStatus) {
                        case 3: break; //连接断开
                        case 4: break;//过载
                        case 5: break;//负重
                    }
                } else {
                    //PeelMode.WeightPell:称重皮
                    // PeelMode.DigitPell:电子皮
                    float peel_data = peelMode.getData();//皮重

                }


            }

            @Override
            public void onrssi(int rssi) {

            }
        });
    }

    /**
     * 各功能(去皮,置零,电子皮,模式切换)
     */
    public void features(Context context) {
        /**
         * 置零
         * result
         * -1:当前版本没有返回值
         * 0:设备未连接
         * 1:置零成功
         * 2:有皮重不允许置零
         * 3:置零超出范围
         * 0xff:超时
         */
        int result = LScaleHelper.getInstance(context).zero();

        /**
         * 去皮
         * -1:当前版本没有返回值
         * 0:设备未连接
         * 1:去皮成功
         * 2:零点错误报警
         * 3:超过去皮范围
         * 4:有数字皮,不可去称重皮
         * 5:重量不稳定
         * 0xff:超时
         */
        int result2 = LScaleHelper.getInstance(context).tare();


        /**
         * 设置电子皮(框皮)
         * 0:设备未连接
         * 1:成功
         * 2:有称重皮存在,添加数字皮失败
         * 3:重量不稳定
         * 4:零点错误
         * 5:去皮超范围
         * 0xff:超时
         * 0xee:旧版本没有成功状态返回
         */
        int result3 = LScaleHelper.getInstance(context).setPeel("2.16");

        /**
         * 清皮
         * -2:当前版本不支持此功能
         * 0:设备未连接
         * 1:清皮成功
         * 0xff:超时
         */
        int result4 = LScaleHelper.getInstance(context).clearTare();

        /**
         * 模式切换(WeightMode.ANIMAL,WeightMode.BLANCE)
         * -2:当前版本不支持此功能
         * 0:设备未连接
         * 1:切换成功
         * 0xff:超时
         */
        int result5 = LScaleHelper.getInstance(context).setWeightMode(WeightMode.BLANCE);

        /**
         * 查询当前模式
         * -2:当前版本不支持此功能
         * 0:设备未连接
         * 1:一般模式---不可切换
         * 2:一般模式---可以切换
         * 3:动物模式---不可切换
         * 4:动物模式---可以切换
         * 0xff:超时
         */
        int result6 = LScaleHelper.getInstance(MainApp.context).queryWeightMode();


        /**
         * 获取电子秤电量信息
         * isCharge:是否在重点
         * voltage:当前电压值---4.62为满电量
         * voltage_percent:电量百分比
         */
        LScaleHelper.getInstance(context).getBatteryStatus(new BatteryStatusCallback() {
            @Override
            public void callback(boolean isCharge, double voltage, String voltage_percent) {

            }
        });

        /**
         * 获取连接设备
         */
        BluetoothDevice device = LScaleHelper.getInstance(context).getConnectDevice();



    }


}




























