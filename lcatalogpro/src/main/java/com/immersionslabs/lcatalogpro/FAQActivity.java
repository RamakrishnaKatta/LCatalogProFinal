package com.immersionslabs.lcatalogpro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FAQActivity extends AppCompatActivity {

    RelativeLayout step1, step2, step3, step4, step5, step6, step7, step8, step9, step10, step11, step12, step13, step14, step15, step16, step17, step18, step19, step20, step21;
    TextView step_text1, step_text2, step_text3, step_text4, step_text5_1, step_text5_2, step_text6_1, step_text6_2, step_text7, step_text8, step_text9, step_text10, step_text11, step_text12, step_text13, step_text14_1, step_text14_2, step_text15, step_text16, step_text17, step_text18, step_text19_1, step_text19_2, step_text20, step_text21_1, step_text21_2;
    AppCompatImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12, img13, img14, img15, img16, img17, img18, img19, img20, img21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        Toolbar toolbar = findViewById(R.id.toolbar_faq);
        toolbar.setTitleTextAppearance(this, R.style.LCatalogCustomText_ToolBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);
        img8 = findViewById(R.id.img8);
        img9 = findViewById(R.id.img9);
        img10 = findViewById(R.id.img10);
        img11 = findViewById(R.id.img11);
        img12 = findViewById(R.id.img12);
        img13 = findViewById(R.id.img13);
        img14 = findViewById(R.id.img14);
        img15 = findViewById(R.id.img15);
        img16 = findViewById(R.id.img16);
        img17 = findViewById(R.id.img17);
        img18 = findViewById(R.id.img18);
        img19 = findViewById(R.id.img19);
        img20 = findViewById(R.id.img20);
        img21 = findViewById(R.id.img21);

        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        step4 = findViewById(R.id.step4);
        step5 = findViewById(R.id.step5);
        step6 = findViewById(R.id.step6);
        step7 = findViewById(R.id.step7);
        step8 = findViewById(R.id.step8);
        step9 = findViewById(R.id.step9);
        step10 = findViewById(R.id.step10);
        step11 = findViewById(R.id.step11);
        step12 = findViewById(R.id.step12);
        step13 = findViewById(R.id.step13);
        step14 = findViewById(R.id.step14);
        step15 = findViewById(R.id.step15);
        step16 = findViewById(R.id.step16);
        step17 = findViewById(R.id.step17);
        step18 = findViewById(R.id.step18);
        step19 = findViewById(R.id.step19);
        step20 = findViewById(R.id.step20);
        step21 = findViewById(R.id.step21);

        step1.setTag("down");
        step2.setTag("down");
        step3.setTag("down");
        step4.setTag("down");
        step5.setTag("down");
        step6.setTag("down");
        step7.setTag("down");
        step8.setTag("down");
        step9.setTag("down");
        step10.setTag("down");
        step11.setTag("down");
        step12.setTag("down");
        step13.setTag("down");
        step14.setTag("down");
        step15.setTag("down");
        step16.setTag("down");
        step17.setTag("down");
        step18.setTag("down");
        step19.setTag("down");
        step20.setTag("down");
        step21.setTag("down");

        step_text1 = findViewById(R.id.step1text);
        step_text2 = findViewById(R.id.step2text);
        step_text3 = findViewById(R.id.step3text);
        step_text4 = findViewById(R.id.step4text);
        step_text5_1 = findViewById(R.id.step5text1);
        step_text5_2 = findViewById(R.id.step5text2);
        step_text6_1 = findViewById(R.id.step6text1);
        step_text6_2 = findViewById(R.id.step6text2);
        step_text7 = findViewById(R.id.step7text);
        step_text8 = findViewById(R.id.step8text);
        step_text9 = findViewById(R.id.step9text);
        step_text10 = findViewById(R.id.step10text);
        step_text11 = findViewById(R.id.step11text);
        step_text12 = findViewById(R.id.step12text);
        step_text13 = findViewById(R.id.step13text);
        step_text14_1 = findViewById(R.id.step14text1);
        step_text14_2 = findViewById(R.id.step14text2);
        step_text15 = findViewById(R.id.step15text);
        step_text16 = findViewById(R.id.step16text);
        step_text17 = findViewById(R.id.step17text);
        step_text18 = findViewById(R.id.step18text);
        step_text19_1 = findViewById(R.id.step19text1);
        step_text19_2 = findViewById(R.id.step19text2);
        step_text20 = findViewById(R.id.step20text);
        step_text21_1 = findViewById(R.id.step21text1);
        step_text21_2 = findViewById(R.id.step21text2);

        step_text1.setVisibility(View.GONE);
        step_text2.setVisibility(View.GONE);
        step_text3.setVisibility(View.GONE);
        step_text4.setVisibility(View.GONE);
        step_text5_1.setVisibility(View.GONE);
        step_text5_2.setVisibility(View.GONE);
        step_text6_1.setVisibility(View.GONE);
        step_text6_2.setVisibility(View.GONE);
        step_text7.setVisibility(View.GONE);
        step_text8.setVisibility(View.GONE);
        step_text9.setVisibility(View.GONE);
        step_text10.setVisibility(View.GONE);
        step_text11.setVisibility(View.GONE);
        step_text12.setVisibility(View.GONE);
        step_text13.setVisibility(View.GONE);
        step_text14_1.setVisibility(View.GONE);
        step_text14_2.setVisibility(View.GONE);
        step_text15.setVisibility(View.GONE);
        step_text16.setVisibility(View.GONE);
        step_text17.setVisibility(View.GONE);
        step_text18.setVisibility(View.GONE);
        step_text19_1.setVisibility(View.GONE);
        step_text19_2.setVisibility(View.GONE);
        step_text20.setVisibility(View.GONE);
        step_text21_1.setVisibility(View.GONE);
        step_text21_2.setVisibility(View.GONE);
    }

    //OnClick functions ----------------------------------------------------
    public void step1(View v) {
        if (step1.getTag().toString().equals("down")) {
            step_text1.setVisibility(View.VISIBLE);
            img1.setImageResource(R.mipmap.ic_faq_up);
            step1.setTag("up");
        } else {
            img1.setImageResource(R.mipmap.ic_faq_down);
            step_text1.setVisibility(View.GONE);
            step1.setTag("down");
        }
    }

    public void step2(View v) {
        if (step2.getTag().toString().equals("down")) {
            step_text2.setVisibility(View.VISIBLE);
            img2.setImageResource(R.mipmap.ic_faq_up);
            step2.setTag("up");
        } else {
            img2.setImageResource(R.mipmap.ic_faq_down);
            step_text2.setVisibility(View.GONE);
            step2.setTag("down");
        }
    }

    public void step3(View v) {
        if (step3.getTag().toString().equals("down")) {
            step_text3.setVisibility(View.VISIBLE);
            img3.setImageResource(R.mipmap.ic_faq_up);
            step3.setTag("up");
        } else {
            img3.setImageResource(R.mipmap.ic_faq_down);
            step_text3.setVisibility(View.GONE);
            step3.setTag("down");
        }
    }

    public void step4(View v) {
        if (step4.getTag().toString().equals("down")) {
            step_text4.setVisibility(View.VISIBLE);
            img4.setImageResource(R.mipmap.ic_faq_up);
            step4.setTag("up");
        } else {
            img4.setImageResource(R.mipmap.ic_faq_down);
            step_text4.setVisibility(View.GONE);
            step4.setTag("down");
        }
    }

    public void step5(View v) {
        if (step5.getTag().toString().equals("down")) {
            step_text5_1.setVisibility(View.VISIBLE);
            step_text5_2.setVisibility(View.VISIBLE);
            img5.setImageResource(R.mipmap.ic_faq_up);
            step5.setTag("up");
        } else {
            img5.setImageResource(R.mipmap.ic_faq_down);
            step_text5_1.setVisibility(View.GONE);
            step_text5_2.setVisibility(View.GONE);
            step5.setTag("down");
        }
    }

    public void step6(View v) {
        if (step6.getTag().toString().equals("down")) {
            step_text6_1.setVisibility(View.VISIBLE);
            step_text6_2.setVisibility(View.VISIBLE);
            img6.setImageResource(R.mipmap.ic_faq_up);
            step6.setTag("up");
        } else {
            img6.setImageResource(R.mipmap.ic_faq_down);
            step_text6_1.setVisibility(View.GONE);
            step_text6_2.setVisibility(View.GONE);
            step6.setTag("down");
        }
    }

    public void step7(View v) {
        if (step7.getTag().toString().equals("down")) {
            step_text7.setVisibility(View.VISIBLE);
            img7.setImageResource(R.mipmap.ic_faq_up);
            step7.setTag("up");
        } else {
            img7.setImageResource(R.mipmap.ic_faq_down);
            step_text7.setVisibility(View.GONE);
            step7.setTag("down");
        }
    }

    public void step8(View v) {
        if (step8.getTag().toString().equals("down")) {
            step_text8.setVisibility(View.VISIBLE);
            img8.setImageResource(R.mipmap.ic_faq_up);
            step8.setTag("up");
        } else {
            img8.setImageResource(R.mipmap.ic_faq_down);
            step_text8.setVisibility(View.GONE);
            step8.setTag("down");
        }
    }

    public void step9(View v) {
        if (step9.getTag().toString().equals("down")) {
            step_text9.setVisibility(View.VISIBLE);
            img9.setImageResource(R.mipmap.ic_faq_up);
            step9.setTag("up");
        } else {
            img9.setImageResource(R.mipmap.ic_faq_down);
            step_text9.setVisibility(View.GONE);
            step9.setTag("down");
        }
    }

    public void step10(View v) {
        if (step10.getTag().toString().equals("down")) {
            step_text10.setVisibility(View.VISIBLE);
            img10.setImageResource(R.mipmap.ic_faq_up);
            step10.setTag("up");
        } else {
            img10.setImageResource(R.mipmap.ic_faq_down);
            step_text10.setVisibility(View.GONE);
            step10.setTag("down");
        }
    }

    public void step11(View v) {
        if (step11.getTag().toString().equals("down")) {
            step_text11.setVisibility(View.VISIBLE);
            img11.setImageResource(R.mipmap.ic_faq_up);
            step11.setTag("up");
        } else {
            img11.setImageResource(R.mipmap.ic_faq_down);
            step_text11.setVisibility(View.GONE);
            step11.setTag("down");
        }
    }

    public void step12(View v) {
        if (step12.getTag().toString().equals("down")) {
            step_text12.setVisibility(View.VISIBLE);
            img12.setImageResource(R.mipmap.ic_faq_up);
            step12.setTag("up");
        } else {
            img12.setImageResource(R.mipmap.ic_faq_down);
            step_text12.setVisibility(View.GONE);
            step12.setTag("down");
        }
    }

    public void step13(View v) {
        if (step13.getTag().toString().equals("down")) {
            step_text13.setVisibility(View.VISIBLE);
            img13.setImageResource(R.mipmap.ic_faq_up);
            step13.setTag("up");
        } else {
            img13.setImageResource(R.mipmap.ic_faq_down);
            step_text13.setVisibility(View.GONE);
            step13.setTag("down");
        }
    }

    public void step14(View v) {
        if (step14.getTag().toString().equals("down")) {
            step_text14_1.setVisibility(View.VISIBLE);
            step_text14_2.setVisibility(View.VISIBLE);
            img14.setImageResource(R.mipmap.ic_faq_up);
            step14.setTag("up");
        } else {
            img14.setImageResource(R.mipmap.ic_faq_down);
            step_text14_1.setVisibility(View.GONE);
            step_text14_2.setVisibility(View.GONE);
            step14.setTag("down");
        }
    }

    public void step15(View v) {
        if (step15.getTag().toString().equals("down")) {
            step_text15.setVisibility(View.VISIBLE);
            img15.setImageResource(R.mipmap.ic_faq_up);
            step15.setTag("up");
        } else {
            img15.setImageResource(R.mipmap.ic_faq_down);
            step_text15.setVisibility(View.GONE);
            step15.setTag("down");
        }
    }

    public void step16(View v) {
        if (step16.getTag().toString().equals("down")) {
            step_text16.setVisibility(View.VISIBLE);
            img16.setImageResource(R.mipmap.ic_faq_up);
            step16.setTag("up");
        } else {
            img16.setImageResource(R.mipmap.ic_faq_down);
            step_text16.setVisibility(View.GONE);
            step16.setTag("down");
        }
    }

    public void step17(View v) {
        if (step17.getTag().toString().equals("down")) {
            step_text17.setVisibility(View.VISIBLE);
            img17.setImageResource(R.mipmap.ic_faq_up);
            step17.setTag("up");
        } else {
            img17.setImageResource(R.mipmap.ic_faq_down);
            step_text17.setVisibility(View.GONE);
            step17.setTag("down");
        }
    }

    public void step18(View v) {
        if (step18.getTag().toString().equals("down")) {
            step_text18.setVisibility(View.VISIBLE);
            img18.setImageResource(R.mipmap.ic_faq_up);
            step18.setTag("up");
        } else {
            img18.setImageResource(R.mipmap.ic_faq_down);
            step_text18.setVisibility(View.GONE);
            step18.setTag("down");
        }
    }

    public void step19(View v) {
        if (step19.getTag().toString().equals("down")) {
            step_text19_1.setVisibility(View.VISIBLE);
            step_text19_2.setVisibility(View.VISIBLE);
            img19.setImageResource(R.mipmap.ic_faq_up);
            step19.setTag("up");
        } else {
            img19.setImageResource(R.mipmap.ic_faq_down);
            step_text19_1.setVisibility(View.GONE);
            step_text19_2.setVisibility(View.GONE);
            step19.setTag("down");
        }
    }

    public void step20(View v) {
        if (step20.getTag().toString().equals("down")) {
            step_text20.setVisibility(View.VISIBLE);
            img20.setImageResource(R.mipmap.ic_faq_up);
            step20.setTag("up");
        } else {
            img20.setImageResource(R.mipmap.ic_faq_down);
            step_text20.setVisibility(View.GONE);
            step20.setTag("down");
        }
    }

    public void step21(View v) {
        if (step21.getTag().toString().equals("down")) {
            step_text21_1.setVisibility(View.VISIBLE);
            step_text21_2.setVisibility(View.VISIBLE);
            img21.setImageResource(R.mipmap.ic_faq_up);
            step21.setTag("up");
        } else {
            img21.setImageResource(R.mipmap.ic_faq_down);
            step_text21_1.setVisibility(View.GONE);
            step_text21_2.setVisibility(View.GONE);
            step21.setTag("down");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }
}