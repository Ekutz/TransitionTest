# TransitionTest
Shared element Transition test

#Shared Element Transition

##Shared Element Transition 이란?
Android Developer에서는 [**사용자 지정 애니메이션**](https://developer.android.com/training/material/animations.html?hl=ko)으로 불리운다.

쉽게 설명하자면 이전 액티비티나 프래그먼트에서 쓰였던 뷰를 재활용하여 좀 더 동적인 움직임을 보여주는 방법 중 하나이다.

머테리얼 디자인을 기점으로 동작하기 때문에 Api 21 미만은 사용할 수 없다는 단점이 있지만 좀 더 부드럽게 움직이는 어플을 만들 수 있다는 장점이 있다.

##예시
아래 예시처럼 동적으로 뷰들을 재활용 할 수 있다.

![예시1](https://i.stack.imgur.com/qvkGp.gif)

---
##적용 방식
###1. 애니메이션 설정
어떠한 애니메이션 형식으로 유동적인 뷰의 재활용을 할지 정한다.  
app/res 에서 new Directory로 transition을 생성한 후 Transition resource file을 새로 만든다

**app/res/transition/custom_trans.xml**

```
<transitionSet xmlns:android="http://schemas.android.com/apk/res/android">
	<changeBounds/>
</transitionSet>	
	
* changeBounds - 대상 뷰의 레이아웃 경계에서 변경을 애니메이트합니다.
* changeClipBounds - 대상 뷰의 클리핑 경계에서 변경을 애니메이트합니다.
* changeTransform - 대상 뷰의 배율 및 회전 변경을 애니메이트합니다.
* changeImageTransform - 대상 이미지의 크기 및 배율 변경을 애니메이트합니다.
```

###2. 스타일 설정
어플 전체 혹은 각 액티비티에 적용되는 스타일에 아이템을 추가하여 애니메이션이 가능하도록 설정한다.  
app/res/values/styles.xml 을 편집한다

**app/res/values/styles.xml**

```
<style name="커스텀 스타일" parent="Theme.AppCompat.맘에 드는 테마">
	<item name="android:windowContentTransitions">true</item>
	// 액티비티 내의 뷰들이 Transition이 가능하도록 한다
	<item name="android:windowSharedElementEnterTransition">트랜지션.xml</item>
	// 액티비티 이동 시 입장 애니메이션
	<item name="android:windowSharedElementExitTransition">트랜지션.xml</item>
	// 액티비티 이동 시 퇴장 애니메이션
</style>
```
minSdkVersion이 21 미만일 경우 style.xml에서 3가지 아이템들을 정의할 수 없기 때문에 styles.xml(v21)을 만들어서 적용해 준다.

###3. 스타일 적용
움직일 예정인 액티비티에 모두 스타일을 적용해 준다.  
app/manifests/AndroidManifest.xml 을 편집한다

**app/manifests/AndroidManifest.xml**

```
<activity android:name=".FirstActivity"
	android:theme="style/커스텀 스타일">
	
	...
	
</activity>
<activity android:name=".SecondActivity"
	android:theme="style/커스텀 스타일">
	
	...
	
</activity>
```

###4. UI 설정
뷰들이 움직일 동작의 시작과 끝을 미리 설정한다.
동작을 가지게 될 뷰들은 android:transitionName="" 속성을 추가하여 호칭을 통일해 주어야한다.

**activity_first.xml**

뷰가 움직이기 전의 위치를 지정한다
강제로 화면 밖으로 보내어 다음 액티비티에서 보여지도록 할 수도 있다

```
<ImageView
	android:layout_width="50dp"
	android:layout_height="50dp"
	android:layout_centerHorizontal="true"
	android:src="@mipmap/example"
	android:transitionName="호칭">
</ImageView>
```

**activity_second.xml**

뷰가 움직임을 완료한 위치를 지정한다
강제로 화면 밖으로 보내어 움직임이 끝난 후 보이지 않도록 할 수도 있다.

```
<ImageView
	android:layout_width="100dp"
	android:layout_height="100dp"
	android:layout_centerInParent="true"
	android:src="@mipmap/example"
	android:transitionName="호칭">
</ImageView>
```

###5. 코드 적용
설정한 애니메이션 및 스타일이 실제로 동작하도록 코드를 작성한다.


**FirstActivity.java**

**움직일 뷰가 하나일 때**

```
Intent i = new Intent(FirstActivity.this, SecondActivity.class);

if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
	뷰1, "호칭1");
	
	startActivity(i, options.toBundle());
} else {
	// makeSceneTransitionAnimation 역시 Api 21 이상에서만 동작하기 때문에 분기를 나눈다
}
```

**움직일 뷰가 여러개 일 때**

```
Intent i = new Intent(FirstActivity.this, SecondActivity.class);

if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                            Pair.create((View)뷰1, "호칭1"),
                            Pair.create((View)뷰2, "호칭2"),
                            Pair.create((View)뷰3, "호칭3"));
	
	startActivity(i, options.toBundle());
} else {
	// makeSceneTransitionAnimation 역시 Api 21 이상에서만 동작하기 때문에 분기를 나눈다
}
```

###6. 되돌리기
위 코드를 그대로 적용할 시 액티비티 상단 횡중앙 50X50의 이미지가 액티비티 정중앙 100X100 사이즈로 변하는 것을 볼 수 있다.  
아무런 조치를 취하지 않으면 백버튼을 눌렀을 때 이전처럼 부자연스러운 액티비티의 변화가 일어난다.  
이를 방지하기 위한 코드를 작성해야 한다.

**SecondActivity.java**

```
@Override
	public void onBackPressed() {
		supportFinishAfterTransition();
//        super.onBackPressed();
    }
```

-----

###7. 문제점
사실 멋지게 어플을 만들때 말고는 큰 쓸모도 없는 이 기능이 문제점도 많다는게 참 아쉽다.

####a. 이동의 제약
Activitiy <-> Acitivity (O)  
Fragment <-> Fragment (O)  
Activity <-> Activity + Fragment (X)
이동 후 키보드가 올라올 경우 (사용할 수 있으나 애니메이션 오류)

####b. 오류 찾기의 어려움
1 ~ 6 까지의 일련의 과정을 수행하던 중 단 한가지만 어긋나더라도 Transition은 동작하지 않는다.  
게다가 안드로이드는 이를 오류로 인식하지 않고 아무런 조치를 취하지 않는다.


-----
그럼에도 불구하고 상당히 있어보이는 어플을 만들기에는 손색없는 기능임이 확실하다.

테스트 코드 : []()
