package com.example.meecos.Fragment.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.meecos.Activity.MainActivity
import com.example.meecos.Fragment.Base.BaseFragment
import com.example.meecos.R

class HomeFragment : BaseFragment(), TextWatcher {

    var place = "ここに入力値が表示"

//    companion object {
//        fun newInstance(place: String): HomeFragment {
//            val fragment = HomeFragment()
//            fragment.place = place
//            return fragment
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        setTitle("MEECOS")

        val placeText = view.findViewById<EditText>(R.id.place)
        placeText.addTextChangedListener(this);

        val mapTestButton = view.findViewById<Button>(R.id.map_test)
        mapTestButton.setOnClickListener(onMapTestButton)

        val showPlace = view.findViewById<TextView>(R.id.show_place)
        showPlace.text = this.place

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //リストビューに表示するリストを手動で作成
        val texts = arrayOf("abc ", "bcd", "cde")


        val listView = view.findViewById<ListView>(R.id.today_schedule)

        // simple_list_item_1 は、 もともと用意されている定義済みのレイアウトファイルのID
        val arrayAdapter = ArrayAdapter(requireActivity().applicationContext,
            android.R.layout.simple_list_item_1, texts)

        listView.adapter = arrayAdapter
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
    }

    override fun afterTextChanged(s: Editable) {
        // テキスト変更後に変更されたテキストを取り出す
        val inputStr = s.toString()
        place = inputStr
    }

    private val onMapTestButton = View.OnClickListener {
        connectGoogleMap(this.place)
//        val main = activity as MainActivity
//        main.replaceFragment(HomeFragment.newInstance(place))

    }

    private fun connectGoogleMap(place: String){

        val uri = Uri.parse("https://www.google.com/maps/dir/34.6882048,135.4989568/$place")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)

//        // Create a Uri from an intent string. Use the result to create an Intent.
//        val gmmIntentUri = Uri.parse("geo:0,0?q=$place")
//
//        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//        // Make the Intent explicit by setting the Google Maps package
//        mapIntent.setPackage("com.google.android.apps.maps")
//
//        // Attempt to start an activity that can handle the Intent
//        startActivity(mapIntent)
    }
}