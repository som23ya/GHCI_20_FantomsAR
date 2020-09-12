package com.example.fantomar.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fantomar.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.net.URI;

public class ar_back extends AppCompatActivity {
    private ArFragment arFragment;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar_back);

        arFragment= (ArFragment) getSupportFragmentManager().findFragmentById((R.id.ux_fragment));
         arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) ->
         {
             Anchor anchor= hitResult.createAnchor();
             ModelRenderable.builder().setSource(this, Uri.parse("model.sfb"))
                     .build()
                     .thenAccept(modelRenderable -> addModelToScene(anchor,modelRenderable ))
                     .exceptionally(throwable -> {
                         AlertDialog.Builder builder= new AlertDialog.Builder(this);
                         builder.setMessage(throwable.getMessage()).show();
                         return null;
                     });
      });
 }
 private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode=new AnchorNode(anchor);
        //automatically positions the anchor on the real world
        TransformableNode transformableNode= new TransformableNode( arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().onAddChild(anchorNode);
        transformableNode.select();
    }
}