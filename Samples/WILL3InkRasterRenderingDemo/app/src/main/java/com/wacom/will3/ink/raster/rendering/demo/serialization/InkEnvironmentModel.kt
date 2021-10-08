/*
 * Copyright (C) 2020 Wacom.
 * Use of this source code is governed by the MIT License that can be found in the LICENSE file.
 */
package com.wacom.will3.ink.raster.rendering.demo.serialization

import android.app.Activity
import android.graphics.PointF
import android.os.Build
import android.util.DisplayMetrics
import android.view.MotionEvent
import com.wacom.ink.format.InkModel
import com.wacom.ink.format.enums.InkInputType
import com.wacom.ink.format.enums.InkSensorMetricType
import com.wacom.ink.format.enums.InkSensorType
import com.wacom.ink.format.enums.InkState
import com.wacom.ink.format.input.*
import com.wacom.ink.format.tree.data.SensorData
import com.wacom.ink.format.util.ScalarUnit
import com.wacom.ink.model.Identifier
import com.wacom.will3.ink.raster.rendering.demo.R
import java.util.*


/**
 * This class contains information about the environment, in order to be saved
 * when serialized a InkModel.
 */
class InkEnvironmentModel(val activity: Activity) {

    var environment: Environment

    var inputDevice: InputDevice

    var inputProviders = hashMapOf<Identifier, InkInputProvider>()

    var sensorContexts = hashMapOf<Identifier, SensorContext>()

    var inputContexts = hashMapOf<Identifier, InputContext>()

    val inputProviderToInputContextMapping = hashMapOf<Identifier, Identifier>()

    val channelsForInput = hashMapOf<Identifier, List<SensorChannel>>()

    val touchChannels = listOf(InkSensorType.X, InkSensorType.Y, InkSensorType.TIMESTAMP)
    val penChannels = listOf(InkSensorType.X, InkSensorType.Y, InkSensorType.TIMESTAMP, InkSensorType.PRESSURE, InkSensorType.ALTITUDE, InkSensorType.AZIMUTH)

    init {
        // Initialize environment
        environment = Environment()
        environment.putProperty("os.name", "android")
        environment.putProperty("os.version.name", Build.VERSION.CODENAME)
        environment.putProperty("os.version.code", Build.VERSION.SDK_INT.toString())
        environment.putProperty("os.version.incremental", Build.VERSION.INCREMENTAL)
        environment.putProperty("os.version.release", Build.VERSION.RELEASE)
        environment.putProperty("wacom.ink.sdk.name", activity.getString(R.string.sdk_name))
        environment.putProperty("wacom.ink.sdk.version", activity.getString(R.string.sdk_version))

        // Initialize InputDevice
        inputDevice = InputDevice()
        inputDevice.putProperty("dev.id", Build.ID)
        inputDevice.putProperty("dev.fingerprint", Build.FINGERPRINT)
        inputDevice.putProperty("dev.manufacturer", Build.MANUFACTURER)
        inputDevice.putProperty("dev.brand", Build.BRAND)
        inputDevice.putProperty("dev.model", Build.MODEL)
        inputDevice.putProperty("dev.board", Build.BOARD)
        inputDevice.putProperty("dev.hardware", Build.HARDWARE)
        inputDevice.putProperty("dev.codename", Build.DEVICE)
        inputDevice.putProperty("dev.display", Build.DISPLAY)
        inputDevice.putProperty("dev.host", Build.HOST)
    }

    fun createSensorData(event: MotionEvent): Pair<SensorData, List<SensorChannel>> {
        val toolType = when (event.getToolType(0)) {
            MotionEvent.TOOL_TYPE_STYLUS -> InkInputType.PEN
            MotionEvent.TOOL_TYPE_FINGER -> InkInputType.TOUCH
            MotionEvent.TOOL_TYPE_MOUSE -> InkInputType.MOUSE
            else -> InkInputType.PEN
        }

        var provider: InkInputProvider? = null
        // first look if exist an input provider of the desiree type
        for ((_, existingProvider) in inputProviders) {
            existingProvider.type == toolType
            provider = existingProvider
            break
        }

        if (provider == null) {
            provider = InkInputProvider(toolType)

            // it is possible to add custom define properties to the input provider
            // for example PenID in case exists
            if (toolType == InkInputType.PEN) {
                provider.putProperty("penType", "s-pen") // assuming using Galaxy note s-pen
            }
        }

        var inputContextId = if (!inputProviders.containsKey(provider.id)) {
            inputProviders[provider.id] = provider

            val channels = registerChannels(if (toolType == InkInputType.PEN) penChannels else touchChannels)
            channelsForInput.put(provider.id, channels)

            val sensorChannelsContext = SensorChannelsContext(provider.id, inputDevice.id, channels)
            val sensorContext = SensorContext()
            sensorContext.addSensorChannelsContext(sensorChannelsContext)

            sensorContexts[sensorContext.id] = sensorContext

            val inputContext = InputContext(environment.id, sensorContext.id)

            inputContexts[inputContext.id] = inputContext

            inputProviderToInputContextMapping[provider.id] = inputContext.id

            inputContext.id
        } else {
            val provider = inputProviders[provider.id]!!
            inputProviderToInputContextMapping[provider.id]!!
        }

        var channelList: List<SensorChannel>? = null
        for ((id, channels) in channelsForInput) {
            if (provider.id == id) {
                channelList = channels
            }
        }

        if (channelList == null) {
            channelList = listOf() // empty list to avoid null pointer exceptions
        }

        return Pair<SensorData, List<SensorChannel>>(SensorData(Identifier(UUID.randomUUID().toString()), inputContextId, InkState.PLANE), channelList)
    }


    fun registerInModel(inkModel: InkModel) {
        if (inkModel.inputConfiguration.getEnvironment(environment.id) == null) {
            inkModel.inputConfiguration.add(environment)
        }

        if (inkModel.inputConfiguration.getInputDevice(inputDevice.id) == null) {
            inkModel.inputConfiguration.add(inputDevice)
        }


        for (sensorContext in sensorContexts.values) {
            if (inkModel.inputConfiguration.getSensorContext(sensorContext.id) == null) {
                inkModel.inputConfiguration.add(sensorContext)
            }
        }

        for (inputContext in inputContexts.values) {
            if (inkModel.inputConfiguration.getInputContext(inputContext.id) == null) {
                inkModel.inputConfiguration.add(inputContext)
            }
        }

        for (inputProvider in inputProviders.values) {
            if (inkModel.inputConfiguration.getInputProvider(inputProvider.id) == null) {
                inkModel.inputConfiguration.add(inputProvider)
            }
        }
    }

    private fun registerChannels(inkSensorTypeUris: List<String>): MutableList<SensorChannel> {
        val precision = 2

        val channels = mutableListOf<SensorChannel>()

        val dimensions = getScreenDimensions()

        for (type in inkSensorTypeUris) {
            val channel = when (type) {
                InkSensorType.X -> SensorChannel(
                    InkSensorType.X,
                    InkSensorMetricType.LENGTH,
                    ScalarUnit.INCH,
                    0.0f,
                    dimensions.x,
                    precision
                )
                InkSensorType.Y -> SensorChannel(
                    InkSensorType.Y,
                    InkSensorMetricType.LENGTH,
                    ScalarUnit.INCH,
                    0.0f,
                    dimensions.y,
                    precision
                )
                InkSensorType.Z -> SensorChannel(
                    InkSensorType.Z,
                    InkSensorMetricType.LENGTH,
                    ScalarUnit.DIP,
                    0.0f,
                    0.0f,
                    precision
                )
                InkSensorType.TIMESTAMP -> SensorChannel(
                    InkSensorType.TIMESTAMP,
                    InkSensorMetricType.TIME,
                    ScalarUnit.MILLISECOND,
                    0.0f,
                    0.0f,
                    0
                )
                InkSensorType.PRESSURE -> SensorChannel(
                    InkSensorType.PRESSURE,
                    InkSensorMetricType.NORMALIZED,
                    ScalarUnit.NORMALIZED,
                    0.0f,
                    1.0f,
                    precision
                )
                InkSensorType.RADIUS_X -> SensorChannel(
                    InkSensorType.RADIUS_X,
                    InkSensorMetricType.LENGTH,
                    ScalarUnit.DIP,
                    0.0f,
                    0.0f,
                    precision
                )
                InkSensorType.RADIUS_Y -> SensorChannel(
                    InkSensorType.RADIUS_Y,
                    InkSensorMetricType.LENGTH,
                    ScalarUnit.DIP,
                    0.0f,
                    0.0f,
                    precision
                )
                InkSensorType.ALTITUDE -> SensorChannel(
                    InkSensorType.ALTITUDE,
                    InkSensorMetricType.ANGLE,
                    ScalarUnit.RADIAN,
                    0.0f,
                    (Math.PI/2).toFloat(),
                    precision
                )
                InkSensorType.AZIMUTH -> SensorChannel(
                    InkSensorType.AZIMUTH,
                    InkSensorMetricType.ANGLE,
                    ScalarUnit.RADIAN,
                    -(Math.PI/2).toFloat(),
                    (Math.PI/2).toFloat(),
                    precision
                )
                InkSensorType.ROTATION -> SensorChannel(
                    InkSensorType.ROTATION,
                    InkSensorMetricType.ANGLE,
                    ScalarUnit.RADIAN,
                    0.0f,
                    0.0f,
                    precision
                )
                else -> {
                    throw Exception("Unknown channel type.")
                }
            }
            channels.add(channel)
        }
        return channels
    }

    private fun getScreenDimensions(): PointF {
        val dm = DisplayMetrics()
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        val dens = dm.densityDpi
        val wi = width.toDouble() / dens.toDouble()
        val hi = height.toDouble() / dens.toDouble()

        return PointF(wi.toFloat(), hi.toFloat())
    }
}