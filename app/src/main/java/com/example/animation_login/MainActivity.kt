package com.example.animation_login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.Display
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*

/**
 当一个输入框被点击  这个输入框就获取到焦点focus
 系统自动弹出一个键盘  和 拥有焦点的输入框进行绑定

 InputMethodManager 是系统提供的服务service
 activity.getSystemService
 */

class MainActivity : AppCompatActivity(),TextWatcher,View.OnFocusChangeListener {
    //使用懒加载 定义动画的对象
    //左手掌下移
    val leftHandDownAnim:ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(mLeftHand,"translationY",mLeftHand.height*0.5f).apply {
            duration = 500
        }
    }
    //左手掌上移
    val leftHandUpAnim:ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(mLeftHand,"translationY",0f).apply {
            duration = 500
        }
    }
    //右手掌下移
    val rightHandDownAnim:ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(mRighrHand,"translationY",mRighrHand.height*0.5f).apply {
            duration = 500
        }
    }
    //右手掌上移
    val rightHandUpAnim:ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(mRighrHand,"translationY",0f).apply {
            duration = 500
        }
    }
    //旋转 x方向移动 y方向移动
    //左手臂向上旋转
    val leftArmUpAnim:AnimatorSet by lazy {
      val rotate=  ObjectAnimator.ofFloat(mLeftArm,"rotation",140f).apply {
            duration = 500
        }
        //手臂向右移动
      val tx=  PropertyValuesHolder.ofFloat("translationX",dpToPx(48f))
        //手臂向上移动
      val ty=  PropertyValuesHolder.ofFloat("translationY",-dpToPx(40f))
      val translate=  ObjectAnimator.ofPropertyValuesHolder(mLeftArm,tx,ty)

        AnimatorSet().apply {
            playTogether(rotate,translate)
            duration = 500
        }
    }
    //左手臂向下旋转
    val leftArmDownAnim:AnimatorSet by lazy {
      val rotate=  ObjectAnimator.ofFloat(mLeftArm,"rotation",0f).apply {
            duration = 500
        }
        //手臂向左移动
        val tx=  PropertyValuesHolder.ofFloat("translationX",0f)
        //手臂向下移动
        val ty=  PropertyValuesHolder.ofFloat("translationY",0f)
        val translate=  ObjectAnimator.ofPropertyValuesHolder(mLeftArm,tx,ty)

        AnimatorSet().apply {
            playTogether(rotate,translate)
            duration = 500
        }
    }
    //右手臂向上旋转
    val rightArmUpAnim:AnimatorSet by lazy {
    val rotate=    ObjectAnimator.ofFloat(mRightArm,"rotation",-140f).apply {
            duration = 500
        }
        //手臂向左移动
        val tx=  PropertyValuesHolder.ofFloat("translationX",-dpToPx(48f))
        //手臂向上移动
        val ty=  PropertyValuesHolder.ofFloat("translationY",-dpToPx(40f))
        val translate=  ObjectAnimator.ofPropertyValuesHolder(mRightArm,tx,ty)

        AnimatorSet().apply {
            playTogether(rotate,translate)
            duration = 500
        }
    }
    //右手臂向下旋转
    val rightArmDownAnim:AnimatorSet by lazy {
     val rotate=   ObjectAnimator.ofFloat(mRightArm,"rotation",0f).apply {
            duration = 500
        }
        //手臂向右移动
        val tx=  PropertyValuesHolder.ofFloat("translationX",0f)
        //手臂向下移动
        val ty=  PropertyValuesHolder.ofFloat("translationY",0f)
        val translate=  ObjectAnimator.ofPropertyValuesHolder(mRightArm,tx,ty)

        AnimatorSet().apply {
            playTogether(rotate,translate)
            duration = 500
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //输入框设置内容改变的监听器
        mName.addTextChangedListener(this)
        mPassword.addTextChangedListener(this)

        //输入焦点改变的监听事件
        mName.onFocusChangeListener = this
        mPassword.onFocusChangeListener = this

    }

    //BlurKit会动态虚化
    override fun onStart() {
        super.onStart()
        //开始虚化
        blurLayout.startBlur()
    }

    override fun onResume() {
        super.onResume()
        //暂停虚化
        blurLayout.pauseBlur()
    }

    override fun afterTextChanged(s: Editable?) {
        //当两个输入框都有内容才能点击
        mLogin.isEnabled = mName.text.isNotEmpty() &&mPassword.text.isNotEmpty()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action==MotionEvent.ACTION_DOWN){
             //隐藏键盘
         hideKeyboard()
        }
        return super.onTouchEvent(event)
    }

    //隐藏键盘
    private fun hideKeyboard(){
        //取消焦点
        if(mName.hasFocus()){
            mName.clearFocus()
        }
        if(mPassword.hasFocus()){
            mPassword.clearFocus()
        }
       //获取管理输入的Manager
      val inputMethodManager=
          getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(mPassword.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    //焦点发生改变的事件
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
       if(v == mPassword){
         if(hasFocus){
          coverEye()
         }else{
          openEye()
         }
       }
    }

    /**
     遮住眼睛
     两只手掌同时向下移动  ->两只手同时旋转
     AnimatorSet
     实现创建一次有两种方法
        懒加载
        定义一个变量  定义一个方法  在方法中判断这个变量是否有值
     */
    private fun coverEye(){
           AnimatorSet().apply {
               play(leftHandDownAnim)
                   .with(rightHandDownAnim)
                   .before(leftArmUpAnim)
                   .before(rightArmUpAnim)
               start()
           }
    }

    /**
     打开眼睛
     两只手同时旋转  ->两个手掌同时向上移动
     */
    private fun openEye(){
        AnimatorSet().apply {
            play(leftArmDownAnim)
                .with(rightArmDownAnim)
                .before(leftHandUpAnim)
                .before(rightHandUpAnim)
            start()
        }
    }

    //将dp值转化为像素
    private fun dpToPx(dp:Float):Float{
        val display= DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)
        return display.density*dp
    }
}
