package com.jeff.startproject.ui.drag

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.Toast
import com.jeff.startproject.databinding.ActivityDragHelperBinding
import com.log.JFLog
import com.ui.base.BaseActivity

class DragHelperActivity : BaseActivity<ActivityDragHelperBinding>() {

    override fun getViewBinding(): ActivityDragHelperBinding {
        return ActivityDragHelperBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.statusBarColor = Color.WHITE
            window.decorView.windowInsetsController?.apply {
                setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)
            }
        }

        binding.dragHelperBtn.setOnLongClickListener { view ->
            // Create a new ClipData.
            // This is done in two steps to provide clarity. The convenience method
            // ClipData.newPlainText() can create a plain text ClipData in one step.

            // Create a new ClipData.Item from the ImageView object's tag.
            val item = ClipData.Item("Hello")

            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This creates a new ClipDescription object within the
            // ClipData and sets its MIME type to "text/plain".
            val dragData = ClipData(
                "WORLD",
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            // Instantiate the drag shadow builder.
            val myShadow = MyDragShadowBuilder(view)

            // Start the drag.
            view.startDragAndDrop(
                dragData,  // The data to be dragged
                myShadow,  // The drag shadow builder
                null,      // No need to use local data
                0          // Flags (not currently used, set to 0)
            )

            // Indicate that the long-click was handled.
            true
        }

        binding.dropArea.setBackgroundColor(Color.LTGRAY)
        binding.dropArea.setOnDragListener { view, event ->
            // Handles each of the expected events.
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    // Determines if this View can accept the dragged data.
                    if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        // As an example of what your application might do, applies a blue color tint
                        // to the View to indicate that it can accept data.
                        binding.stateLabel.text = "ACTION_DRAG_STARTED"
                        binding.dropArea.setBackgroundColor(Color.LTGRAY)

                        // Invalidate the view to force a redraw in the new tint.
                        view.invalidate()

                        // Returns true to indicate that the View can accept the dragged data.
                        true
                    } else {
                        // Returns false to indicate that, during the current drag and drop operation,
                        // this View will not receive events again until ACTION_DRAG_ENDED is sent.
                        false
                    }
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    // Applies a green tint to the View.
                    binding.stateLabel.text = "ACTION_DRAG_ENTERED"
                    binding.dropArea.setBackgroundColor(Color.GREEN)

                    // Invalidates the view to force a redraw in the new tint.
                    view.invalidate()

                    // Returns true; the value is ignored.
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {
                    binding.locLabel.text = "X: ${event.x}, Y: ${event.y}"
                    true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    // Resets the color tint to blue.
                    binding.stateLabel.text = "ACTION_DRAG_EXITED"
                    binding.dropArea.setBackgroundColor(Color.LTGRAY)

                    // Invalidates the view to force a redraw in the new tint.
                    view.invalidate()

                    // Returns true; the value is ignored.
                    true
                }

                DragEvent.ACTION_DROP -> {
                    // Gets the item containing the dragged data.
                    val item: ClipData.Item = event.clipData.getItemAt(0)

                    // Gets the text data from the item.
                    val dragData = item.text

                    // Displays a message containing the dragged data.
                    Toast.makeText(this, "Dragged data is $dragData", Toast.LENGTH_LONG).show()

                    // Turns off any color tints.
                    binding.stateLabel.text = "ACTION_DROP"

                    // Invalidates the view to force a redraw.
                    view.invalidate()

                    // Returns true. DragEvent.getResult() will return true.
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    // Turns off any color tinting.
                    binding.stateLabel.text = "ACTION_DRAG_ENDED"

                    // Invalidates the view to force a redraw.
                    view.invalidate()

                    // Does a getResult(), and displays what happened.
                    when (event.result) {
                        true -> Toast.makeText(this, "The drop was handled.", Toast.LENGTH_LONG)
                        else -> Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_LONG)
                    }.show()

                    // Returns true; the value is ignored.
                    true
                }

                else -> {
                    // An unknown action type was received.
                    JFLog.e("DragDrop Example", "Unknown action type received by View.OnDragListener.")
                    false
                }
            }
        }
    }
}
