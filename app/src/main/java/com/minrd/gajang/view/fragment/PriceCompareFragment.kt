package com.minrd.gajang.view.fragment

import android.nfc.Tag
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.minrd.gajang.R
import com.minrd.gajang.base.BaseFragment
import com.minrd.gajang.data.model.ResponseNecessariesData
import com.minrd.gajang.databinding.FragmentPriceCompareBinding
import com.minrd.gajang.util.GajangApplication
import com.minrd.gajang.view.adapter.NearByAdapter
import com.minrd.gajang.view.adapter.PriceCompareAdapter
import com.minrd.gajang.viewmodel.MainViewModel

class PriceCompareFragment : BaseFragment<FragmentPriceCompareBinding>(R.layout.fragment_price_compare) {

    private lateinit var viewModel : MainViewModel

    override fun FragmentPriceCompareBinding.onCreateView(){
        activity?.run{
            viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(MainViewModel::class.java)
        }
        setRecyclerViewAdapter()
    }

    override fun FragmentPriceCompareBinding.onViewCreated() {
        setNecessarySpinner()
    }

    private fun setRecyclerViewAdapter(){
        val priceCompareAdapter = PriceCompareAdapter(viewModel.currentNecessariesData.value!!)
        binding.priceCompareRcv.adapter = priceCompareAdapter
    }

    private fun setNecessarySpinner(){
        val necessariesItemData = resources.getStringArray(R.array.item_array)
        val priceCompareSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, necessariesItemData)
        val priceCompareSpinner = binding.priceCompareMarketSpinner
        val priceCompareMarketImage = binding.priceCompareMarketImage

        priceCompareSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        priceCompareSpinner.adapter = priceCompareSpinnerAdapter
        priceCompareSpinner.setSelection(viewModel.selectedNecessary.value!!)
        priceCompareSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                binding.priceCompareItemName.text = binding.priceCompareMarketSpinner.selectedItem.toString()

                var whatItem = binding.priceCompareMarketSpinner.selectedItem.toString()
                val itemName = whatIsItemSelected(whatItem)
                val imageResourceId: Int = resources.getIdentifier(itemName, "drawable",requireContext().packageName)
                priceCompareMarketImage.setImageResource(imageResourceId)



                var sub : ArrayList<ResponseNecessariesData> = viewModel.currentNecessariesData.value!!
                var dataList: MutableList<ResponseNecessariesData> = ArrayList()
                Log.d("ㄱㅓ주지",GajangApplication.prefs.getString("LivingAreaString", "null"))
                sub.forEach {
                    val name = it.A_NAME
                    val area = it.M_GU_NAME
                    if (name != null) {
                        if (name.contains(priceCompareSpinner.selectedItem.toString()) and area!!.contains(GajangApplication.prefs.getString("LivingAreaString", "null"))) {
                            dataList.add(it)
                        }
                    }
                }
                sub.forEach {
                    val name = it.A_NAME
                    val area = it.M_GU_NAME
                    if (name != null) {
                        if (name.contains(priceCompareSpinner.selectedItem.toString()) and !(area!!.contains(GajangApplication.prefs.getString("LivingAreaString", "null")))) {
                            dataList.add(it)
                        }
                    }
                }

//
                val priceCompareRecyclerViewAdapter = PriceCompareAdapter(dataList)
                binding.priceCompareRcv.adapter = priceCompareRecyclerViewAdapter

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        fun whatIsSelectedOnSpinner(){

        }
    }


    private fun whatIsItemSelected(itemValue:String):String{
        val itemValueName: String = when(itemValue){
            "고등어" -> "fish_icon"
            "오징어" -> "squid"
            "명태" -> "pollock"
            "조기" -> "jogi"
            "달걀" -> "eggs"
            "닭고기" -> "chicken"
            "돼지고기" -> "pig"
            "쇠고기" -> "cow"
            "애호박" -> "greenpumkin"
            "오이" -> "cucumber"
            "상추" -> "lettuce"
            "양파" -> "onion"
            "무" -> "radish"
            "배추" -> "cabbage"
            "배" -> "pear"
            else -> "apple" //사과에 대응함
        }
        return itemValueName
    }
}