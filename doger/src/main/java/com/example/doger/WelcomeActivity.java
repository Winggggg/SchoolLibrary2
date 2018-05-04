package com.example.doger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class WelcomeActivity extends Activity {

	private RelativeLayout rl_welcome;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				startActivity(new Intent(WelcomeActivity.this,
						TabBaseActivity.class));
				finish();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		rl_welcome = (RelativeLayout) findViewById(R.id.rl_welcome);
		// 展示欢迎动画界面
		showWelcomeAnimation();
		// 欢迎动画持续三秒后退出
		handler.sendEmptyMessageDelayed(1, 3000);
	}

	// 展示欢迎动画界面
	private void showWelcomeAnimation() {
		// 创建缩放动画
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setDuration(2000);
//		// 创建旋转动画
//		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
//		rotateAnimation.setDuration(2000);
		// 创建透明度动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		// 创建复合动画
		AnimationSet animationSet = new AnimationSet(true);
		animationSet.addAnimation(alphaAnimation);
		//animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(scaleAnimation);
		// 启动动画
		rl_welcome.startAnimation(animationSet);
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//移除所有未处理的消息
		handler.removeCallbacksAndMessages(null);
	}
}
