package com.ble.demo.fragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ble.demo.MainApp
import com.ble.mylibrary.interfaces.PeelMode
import com.ble.mylibrary.interfaces.WeightUpdateCallback
import com.ble.mylibrary.outdevice.LScaleHelper
import com.ble.demo.R
import com.ble.demo.databinding.BleConnBinding
import com.ble.demo.entity.EventMsg
import com.ble.demo.utils.DateUtil
import com.ble.demo.utils.T
import com.ble.demo.view.PopBlueSearchPop
import com.ble.demo.viewmodel.BleModel
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal

class BDFragment : Fragment() {
    var binding: BleConnBinding? = null
    private var model: BleModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bd, container, false)
        model = ViewModelProvider(requireActivity()).get(BleModel::class.java)
        binding?.model = model
       // model!!.serialcode = ""
        model!!.rssi = -1
        model!!.voltage = -1.0
        //initData()
        return binding?.root
    }


    private fun resetUi() {
        binding!!.tvName.text = "名称: "
        binding!!.tvAddress.text = "地址: "
        binding!!.tvRssi.text = "rssi: "
        binding!!.tvBatter.text = "电压: "
        binding!!.tvState.text = "状态: "
        binding!!.tvStable.text = "稳定信号: "
        binding!!.tvZeroLight.text = "零点指示灯: "
        binding!!.tvPeelLight.text = "去皮指示灯: "
        binding!!.tvPeelData.text = "去皮量: "
        binding!!.tvCharge.text = "充电: "
        binding!!.tvData.text = "数据: "
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LScaleHelper.getInstance(requireContext()).setWUCallback(object : WeightUpdateCallback {
            @SuppressLint("SetTextI18n")
            override fun onWeightUpdate(
                weight: BigDecimal?,
                isStable: Boolean,
                isTared: Boolean,
                isZero: Boolean,
                peelMode: PeelMode?,
                dataStatus: Int,
                error: Boolean
            ) {
                if (error) {
                    when (dataStatus) {
                        3 -> {
                            binding!!.tvState.text = "状态: 断开"
                            EventBus.getDefault().post(EventMsg("dis"))
                        }
                        4 -> { //过载
                            binding!!.tvState.text = "状态: 过载"
                        }
                        5 -> {
                            binding!!.tvState.text = "状态: 负重"
                        }
                    }
                } else {
                    binding!!.tvState.text = "状态: 工作"
                    binding!!.tvData.text = "数据: " + weight?.toPlainString()
                    binding!!.tvStable.text = "稳定信号:$isStable"
                    binding!!.tvZeroLight.text = "零点指示灯:$isZero"
                    binding!!.tvPeelLight.text = "去皮指示灯:$isTared"
                    if (peelMode == null) {
                        binding!!.tvPeelData.text = "去皮量: "
                    } else if (peelMode == PeelMode.WeightPell) {
                        binding!!.tvPeelData.text = "去皮量(称重皮): " + peelMode.data
                    } else if (peelMode == PeelMode.DigitPell) {
                        binding!!.tvPeelData.text = "去皮量(电子皮): " + peelMode.data
                    }
                    if (dataStatus == 6) {
                        T.showShortCenter("外卖订单")
                    }
                }
            }

            override fun onrssi(rssi: Int) {
                binding!!.tvRssi.text = "rssi: $rssi dbm"
                model!!.rssi = rssi
            }
        })
        LScaleHelper.getInstance(requireContext())
            .getBatteryStatus { isCharge, voltage, voltage_percent ->
                binding!!.tvCharge.text = "充电: $isCharge"
                binding!!.tvBatter.text = "电压: $voltage( $voltage_percent% )"
                model!!.voltage = voltage
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            PopBlueSearchPop.Builder(requireContext()).canConnect(true).setModel(model!!)
                .setOnSelectCallback { result, device ->
                    updateConnectResult(result, device)
                }.build().show_center()
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun updateConnectResult(result: Int, device: BluetoothDevice?) {
        when (result) {
            1 -> {
                binding!!.tvName.text = "名称: " + device?.name
                binding!!.tvAddress.text = "地址: " + device?.address
                binding!!.tvFlow.append(DateUtil.getDate_hms() + " 连接成功\r\n")
                T.showShortCenter("连接成功")
            }
            2 -> {
                binding!!.tvFlow.append(
                    DateUtil.getDate_hms() + " 连接失败\r\n")
                resetUi()
                T.showShortCenter("连接失败")

            }
            3 -> {
                binding!!.tvFlow.append(
                DateUtil.getDate_hms() + " 连接断开\r\n")
                resetUi()
                T.showShortCenter("连接断开")
            }
        }
    }
}