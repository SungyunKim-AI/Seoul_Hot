package com.inseoul.make_plan

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout.HORIZONTAL
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import com.applikeysolutions.cosmocalendar.view.CalendarView
import com.inseoul.R

class RangePickerActivity(
    var context: Context
) : BaseDialogHelper() {

    override val dialogView: View by lazy{
        LayoutInflater.from(context).inflate(R.layout.activity_range_picker, null)
    }
//        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)
    //        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun create(): AlertDialog {
        initView()
        return super.create()
    }

    val calendar_view: CalendarView by lazy {
        dialogView.findViewById<CalendarView>(R.id.calendar_view)
    }

    val select_btn: ImageView by lazy{
        dialogView.findViewById<ImageView>(R.id.select_btn)
    }
    val cancel_btn: ImageView by lazy{
        dialogView.findViewById<ImageView>(R.id.cancel_btn)
    }

    private fun View.setClickListenerToDialog(func: (() -> Unit)?) =
        setOnClickListener {
            func?.invoke()
            dialog?.dismiss()
        }

    fun selectBtnClickListener(func:(() -> Unit)? = null)
            = with(select_btn){
        setClickListenerToDialog(func)
    }
    fun cancelBtnClickListener(func:(() -> Unit)? = null)
        = with(cancel_btn){
        setClickListenerToDialog(func)
    }
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_range_picker)

        val lp = WindowManager.LayoutParams()
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lp.dimAmount = 0.8f
        getWindow().attributes = lp

        initView()

        select_btn.setOnClickListener(positiveListener)
        cancel_btn.setOnClickListener(negativeListener)

    }
    */
    // Calendar Customizing

    //    <color name="colorPrimary">#D9F1F1</color>
    //    <color name="colorPrimaryDark">#B6E3E9</color>


    fun initView(){

//        calendar_view.weekendDays = WeekEnd
        calendar_view.weekendDayTextColor = Color.parseColor("#FF0000")

        calendar_view.calendarOrientation = HORIZONTAL
        calendar_view.selectionType = SelectionType.RANGE

        calendar_view.currentDayIconRes = R.drawable.ic_down__arrow

        calendar_view.selectedDayBackgroundStartColor = Color.parseColor("#B6E3E9")
        calendar_view.selectedDayBackgroundEndColor = Color.parseColor("#B6E3E9")
        calendar_view.selectedDayBackgroundColor = Color.parseColor("#D9F1F1")
/*
        select_btn.setOnClickListener {

            var days = calendar_view.selectedDates
            var range = days.size

            val start_calendar = days.get(0)
            val start_day = start_calendar.get(Calendar.DAY_OF_MONTH)
            val start_month = start_calendar.get(Calendar.MONTH)
            val start_year = start_calendar.get(Calendar.YEAR)
            var startDay= start_year.toString() + "년" + (start_month+1) + "월" + start_day + "일"

            val end_calendar = days.get(range - 1)
            val end_day = end_calendar.get(Calendar.DAY_OF_MONTH)
            val end_month = end_calendar.get(Calendar.MONTH)
            val end_year = end_calendar.get(Calendar.YEAR)
            var endDay = end_year.toString() + "년" + (end_month+1) + "월" + end_day + "일"

//            range_text.text = range.toString()
//            start_day_text.text = startDay
//           end_day_text.text = endDay
        }
*/
    }
}
