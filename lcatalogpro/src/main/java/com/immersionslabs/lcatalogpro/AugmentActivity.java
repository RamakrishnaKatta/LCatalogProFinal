package com.immersionslabs.lcatalogpro;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class AugmentActivity extends AppCompatActivity {

    private static final String TAG = AugmentActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private String url = "https://d19x0atvvvutip.cloudfront.net/sfbfiles/";
    private ArFragment arFragment;
    private ModelRenderable renderable;
    String objectname;
    ImageButton imageButton1,imageButton2,imageButton3;
    LinearLayout linearLayout;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objectname = getIntent().getStringExtra("objname");
        if(objectname!=null)
        {
            rendermodel(objectname);
        }
     else
        {
          int layout = getResources().getIdentifier("layout/activity_augment", null, this.getPackageName());
            if (!checkIsSupportedDeviceOrFinish(this)) {
                return;
            }

            setContentView(layout);
            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
            imageButton1=findViewById(R.id.imageButton1);
            imageButton2=findViewById(R.id.imageButton2);
            imageButton3=findViewById(R.id.imageButton3);
            imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
rendermodel("5b1e91199d445f60f1efe5fb");
                }
            });
            imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rendermodel("5b1e8fb64b1d7010cb022c93");
                }
            });
            imageButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
rendermodel("5b1e93764b1d7010cb022c94");
                }
            });
        }

    }
    public void rendermodel(String objectname)    {

       String Url =url+ objectname + ".sfb";
        Uri uri = Uri.parse(Url);
        Log.e(TAG, "uri" + uri);
        if(this.objectname!=null) {
            int layout = getResources().getIdentifier("layout/activity_augment", null, this.getPackageName());
            if (!checkIsSupportedDeviceOrFinish(this)) {
                return;
            }

            setContentView(layout);
            linearLayout=findViewById(R.id.optionslayout);
            linearLayout.setVisibility(View.GONE);
        }
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);


        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        try {
            ModelRenderable.builder()
                    .setSource(this, uri)
                    .build()
                    .thenAccept(renderable -> this.renderable = renderable)
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Unable to load andy " +
                                                "renderable", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (renderable == null) {
                        return;
                    }
                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(renderable);
                    andy.setLocalRotation(Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), 10f));
                    andy.select();
                });
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}