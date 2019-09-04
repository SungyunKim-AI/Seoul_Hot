package com.inseoul.review

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_review_viewpager_bottom_sheet.*
import kotlinx.android.synthetic.main.activity_review_viewpager_bottom_sheet.view.*



class Review_BottomSheetDialog(

    val c: Context,
    val posX:Double,
    val posY:Double,
    val location:String,
    val call:String,
    var like:Int,
    var dislike:Int
): BottomSheetDialogFragment(), OnMapReadyCallback {
    override fun onMapReady(p0: GoogleMap?) {
 //       TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var LatLng = LatLng(posX, posY)
        var marker = MarkerOptions()
        marker.position(LatLng)
        marker.icon(BitmapDescriptorFactory.fromResource(com.inseoul.R.drawable.default_marker))
        p0!!.addMarker(marker)
        p0!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 15f))

    }

    fun getInstance(): BottomSheetDialogFragment {
        return BottomSheetDialogFragment()
    }

    lateinit var mView:View
    lateinit var mapFragment:SupportMapFragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(com.inseoul.R.layout.activity_review_viewpager_bottom_sheet, container, false)

        mapFragment = activity!!.supportFragmentManager
            .findFragmentById(com.inseoul.R.id.bottom_sheet_map) as SupportMapFragment
//        bottom_sheet_map
        mapFragment.getMapAsync(this)

        mView.location.text = location
        mView.call.text = call
        var vlike = ""
        var vdislike = ""
        if(like >= 1000){
            vlike = (like / 1000).toString() + "k"
        }
        if(dislike >= 1000){
            vdislike = (dislike / 1000).toString() + "k"
        }
        mView.like.text = vlike
        mView.dislike.text = vdislike

        mView.bottomSheet_btn.setOnClickListener {
            dismiss()

        }
//        map, location, call, like, dislike
//        return super.onCreateView(inflater, container, savedInstanceState)
        return mView
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        activity!!.supportFragmentManager.beginTransaction().remove(mapFragment).commit()

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity!!.supportFragmentManager.beginTransaction().remove(mapFragment).commit()

    }

}