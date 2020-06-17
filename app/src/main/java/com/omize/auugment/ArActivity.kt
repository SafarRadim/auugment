package com.omize.auugment

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_ar.*
import android.R.attr.y
import android.R.attr.x
import android.view.Display
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.ar.core.*


class ArActivity : AppCompatActivity() {
    private var objType:Int = 0
    private var vpx:Float = 0.0f
    private var vpy:Float = 0.0f
    private var trackbool: Boolean = false
    private var hitbool: Boolean = false
    private lateinit var arFragment: ArFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        arFragment = fragment as ArFragment

        findViewById<ImageButton>(R.id.goBackButton).setOnClickListener {
            finish()
        }

        val viewset = findViewById<View>(android.R.id.content)
        vpx = viewset.x/2
        vpy = viewset.y/2

        arFragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            arFragment.onUpdate(frameTime)
            fragUpdate()
        }

        findViewById<FloatingActionButton>(R.id.showobject).setOnClickListener {
            when(objType) {
                0 -> addObject(Uri.parse("orange.sfb"))
                1 -> addObject(Uri.parse("meat.sfb"))
                2 -> addObject(Uri.parse("bacon.sfb"))
                3 -> addObject(Uri.parse("egg.sfb"))
                4 -> addObject(Uri.parse("egg-cooked.sfb"))
                5 -> addObject(Uri.parse("salt.sfb"))
                6 -> addObject(Uri.parse("potato.sfb"))
                7 -> addObject(Uri.parse("fries.sfb"))
                else -> {
                    addObject(Uri.parse("orange.sfb"))
                    objType = 0
                }
            }
        }

        findViewById<FloatingActionButton>(R.id.changeobject).setOnClickListener {
            when(objType) {
                0, 1, 2, 3, 4, 5, 6 -> objType++
                else -> objType = 0
            }
            Toast.makeText(this, "OBJC: $objType", Toast.LENGTH_SHORT).show()
        }

        fabState(false)
    }

    @SuppressLint("RestrictedApi")
    private fun fabState(enabled: Boolean) {
        if(enabled) {
            showobject.isEnabled = true
            showobject.visibility = View.VISIBLE
        } else {
            showobject.isEnabled = false
            showobject.visibility = View.GONE
        }
    }

    private fun addObject(model: Uri) {
        val frame = arFragment.arSceneView.arFrame
        if (frame != null) {
            val hits = frame.hitTest(vpx, vpy)
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    placeObject(arFragment, hit.createAnchor(), model)
                    break
                }
            }
        }
    }

    private fun placeObject(fragment: ArFragment, anchor: Anchor, model: Uri) {
        ModelRenderable.builder().setSource(fragment.context, model).build().thenAccept { placeit(fragment, anchor, it) }.exceptionally {return@exceptionally null}
    }

    private fun placeit(fragment: ArFragment, anchor: Anchor, renderable: ModelRenderable) {
        val anchorNode = AnchorNode(anchor)
        val objNode = TransformableNode(fragment.transformationSystem)
        objNode.renderable = renderable
        objNode.setParent(anchorNode)
        fragment.arSceneView.scene.addChild(anchorNode)
        objNode.select()
    }

    private fun fragUpdate() {
        updateTracking()
        if(trackbool) {
            val frame = arFragment.arSceneView.arFrame
            val hits: List<HitResult>
            val hitbefore = hitbool
            hitbool = false
            if (frame != null) {
                //need optt + have to be optimized
                hits = frame.hitTest(vpx, vpy)
                for (hit in hits) {
                    val trackable = hit.trackable
                    if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                        hitbool = true
                        break
                    }
                }
            }
            if(hitbefore != hitbool) {fabState(hitbool)}
        }
    }

    private fun updateTracking(): Boolean {
        val frame = arFragment.arSceneView.arFrame
        val trackbefore = trackbool
        trackbool = frame?.camera?.trackingState == TrackingState.TRACKING
        return trackbool != trackbefore
    }
}
