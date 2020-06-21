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
import android.content.Context
import android.content.SharedPreferences
import android.view.Display
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.ar.core.*
import com.google.ar.sceneform.Node
import kotlin.random.Random


class ArActivity : AppCompatActivity() {
    var nom = true
    private var objType:Int = Random.nextInt(0,9)
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

        Toast.makeText(this, "Hledej na zemi kolem sebe objekt!", Toast.LENGTH_LONG).show()

        val viewset = findViewById<View>(android.R.id.content)
        vpx = viewset.x/2
        vpy = viewset.y/2

        arFragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            arFragment.onUpdate(frameTime)
            fragUpdate()
        }
    }

    private fun addtoinv(type: Int) {
        val nii = "O${type}"
        val sharedPref = getSharedPreferences("inv", Context.MODE_PRIVATE) ?: return
        var value = sharedPref.getInt(nii, 0)
        value++
        with (sharedPref.edit()) {
            putInt(nii, value)
            commit()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun fabState(enabled: Boolean) {
        if(nom) {
            Toast.makeText(this, "NALEZEN OBJEKT!", Toast.LENGTH_SHORT).show()
            if (enabled) {
                nom = false

                when (objType) {
                    0 -> addObject(Uri.parse("orange.sfb"))
                    1 -> addObject(Uri.parse("meat.sfb"))
                    2 -> addObject(Uri.parse("bacon.sfb"))
                    3 -> addObject(Uri.parse("egg.sfb"))
                    4 -> addObject(Uri.parse("egg-cooked.sfb"))
                    5 -> addObject(Uri.parse("salt.sfb"))
                    6 -> addObject(Uri.parse("potato.sfb"))
                    7 -> addObject(Uri.parse("fries.sfb"))
                    8 -> addObject(Uri.parse("wheat.sfb"))
                    9 -> addObject(Uri.parse("bread.sfb"))
                }
            }
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
        objNode.setOnTapListener{_, _->
            when (objType) {
                0 -> Toast.makeText(this, "Nalezen pomeranč!", Toast.LENGTH_LONG).show()
                1 -> Toast.makeText(this, "Nalezeno maso!", Toast.LENGTH_LONG).show()
                2 -> Toast.makeText(this, "Nalezena slanina!", Toast.LENGTH_LONG).show()
                3 -> Toast.makeText(this, "Nalezeno vajičko!", Toast.LENGTH_LONG).show()
                4 -> Toast.makeText(this, "Nalezeno volské oko!", Toast.LENGTH_LONG).show()
                5 -> Toast.makeText(this, "Nalezena sůl!", Toast.LENGTH_LONG).show()
                6 -> Toast.makeText(this, "Nalezena brambora!", Toast.LENGTH_LONG).show()
                7 -> Toast.makeText(this, "Nalezeny hranolky!", Toast.LENGTH_LONG).show()
                8 -> Toast.makeText(this, "Nalezena pšenice!", Toast.LENGTH_LONG).show()
                9 -> Toast.makeText(this, "Nalezen chléb!", Toast.LENGTH_LONG).show()
            }
            addtoinv(objType)
            finish()
        }
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
