/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.vector.rendering.demo.manipulation

import com.wacom.ink.model.Identifier
import com.wacom.will3.ink.vector.rendering.demo.vector.WillStroke

class HistoryManager() {

    private var undoStack = mutableListOf<List<WillStroke>>()
    private var redoStack = mutableListOf<List<WillStroke>>()
    private var length = 20 //stack capacity

    fun addStrokes(strokes: MutableList<WillStroke>, clearRedo: Boolean = true) {
        if (clearRedo) {
            redoStack.clear()
        }
        undoStack.add(strokes)

        if (undoStack.size > length) {
            undoStack.removeFirst()
        }
    }

    fun undo(addToRedo: Boolean = true): MutableMap<Identifier, WillStroke>? {
        val result = mutableMapOf<Identifier, WillStroke>()
        if (!undoStack.isEmpty()) {
            if (redoStack.size > length) {
                redoStack.removeFirst()
            }

            if (addToRedo) {
                redoStack.add(undoStack.removeLast())
            } else {
                undoStack.removeLast()
            }
            if (!undoStack.isEmpty()) {
                for (stroke in undoStack.get(undoStack.size-1)) {
                    result.put(stroke.id, stroke)
                }
            }

        }

        return result;
    }

    fun redo(): MutableMap<Identifier, WillStroke>? {
        val result = mutableMapOf<Identifier, WillStroke>()
        if (!redoStack.isEmpty()) {
            if (undoStack.size > length) {
                undoStack.removeFirst()
            }

            undoStack.add(redoStack.get(redoStack.size-1))
            for (stroke in redoStack.removeLast()) {
                result.put(stroke.id, stroke)
            }
        } else if (!undoStack.isEmpty()){
            for (stroke in undoStack.get(undoStack.size-1)) {
                result.put(stroke.id, stroke)
            }
        }

        return result
    }

    fun resetRedo()
    {
        redoStack.clear()
    }

}