/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.manipulation

import com.wacom.ink.manipulation.SpatialModel
import com.wacom.ink.model.InkStroke
import com.wacom.will3.ink.vector.rendering.demo.vector.WillStroke

/**
 * This is not a complete undo manager, it is only intended to be used for restore
 * the state when there is not change on a selection
 */
class UndoManager(val spatialModel: SpatialModel) {

    private var stack = mutableListOf<MutableMap<String, WillStroke>>()
    private var removedStrokes = mutableListOf<InkStroke>()
    private var addedStrokes = mutableListOf<String>()

    fun addStrokes(strokes: MutableMap<String, WillStroke>) {
        val copy = mutableMapOf<String, WillStroke>()
        copy.putAll(strokes)
        stack.add(copy)
    }

    fun removeStroke(stroke: InkStroke) {
        removedStrokes.add(stroke)
    }

    fun addStroke(id: String) {
        addedStrokes.add(id)
    }

    fun undo(): MutableMap<String, WillStroke>? {
        for (added in addedStrokes) {
            spatialModel.remove(added)
        }

        for (remove in removedStrokes) {
            spatialModel.add(remove)
        }

        removedStrokes.clear()
        addedStrokes.clear()

        val item = stack.lastOrNull()
        if (!stack.isEmpty()) {
            stack.removeAt(stack.size - 1)
        }
        return item
    }

    fun reset() {
        stack.clear()
        removedStrokes.clear()
        addedStrokes.clear()
    }

}