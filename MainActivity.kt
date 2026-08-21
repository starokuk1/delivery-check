package com.thaikingdomfruit.app

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

class MainActivity : Activity() {

    private lateinit var kg: EditText
    private lateinit var basket: EditText
    private lateinit var usd1: EditText
    private lateinit var usd2: EditText
    private lateinit var usd3: EditText
    private lateinit var longanPrice: EditText
    private lateinit var packingPerKg: EditText
    private lateinit var basketPrice: EditText
    private lateinit var stickerRate: EditText
    private lateinit var profitPerKg: EditText
    private lateinit var freightThb: EditText

    private lateinit var longanCost: TextView
    private lateinit var packingCost: TextView
    private lateinit var basketCost: TextView
    private lateinit var stickerCost: TextView
    private lateinit var profitCost: TextView
    private lateinit var totalThb: TextView
    private lateinit var totalUsd: TextView
    private lateinit var freightUsd: TextView
    private lateinit var summaryThb: TextView
    private lateinit var summaryUsd: TextView

    private fun money(value: Double): String {
        return String.format(
            Locale.US,
            "%,.0f",
            value
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showPin()
    }

    // =========================================================
    // PIN LOGIN
    // =========================================================

    private fun showPin() {

        val preferences = getSharedPreferences(
            "secure",
            Context.MODE_PRIVATE
        )

        val savedPin = preferences.getString(
            "pin",
            null
        )

        if (savedPin == null) {

            val pinInput = EditText(this)

            pinInput.inputType =
                InputType.TYPE_CLASS_NUMBER or
                InputType.TYPE_NUMBER_VARIATION_PASSWORD

            pinInput.hint = "ตั้ง PIN"

            val dialog = AlertDialog.Builder(this)

            dialog.setTitle("Thai Kingdom Fruit")
            dialog.setMessage("ตั้ง PIN สำหรับเข้า Application")
            dialog.setView(pinInput)
            dialog.setCancelable(false)

            dialog.setPositiveButton(
                "บันทึก"
            ) { _, _ ->

                val newPin =
                    pinInput.text.toString()

                if (newPin.length >= 4) {

                    val editor =
                        preferences.edit()

                    editor.putString(
                        "pin",
                        newPin
                    )

                    editor.commit()

                    buildApp()

                } else {

                    Toast.makeText(
                        this@MainActivity,
                        "PIN ต้องมีอย่างน้อย 4 หลัก",
                        Toast.LENGTH_SHORT
                    ).show()

                    showPin()
                }
            }

            dialog.show()

        } else {

            val pinInput = EditText(this)

            pinInput.inputType =
                InputType.TYPE_CLASS_NUMBER or
                InputType.TYPE_NUMBER_VARIATION_PASSWORD

            pinInput.hint = "PIN"

            val dialog = AlertDialog.Builder(this)

            dialog.setTitle("Thai Kingdom Fruit")
            dialog.setView(pinInput)
            dialog.setCancelable(false)

            dialog.setPositiveButton(
                "เข้าใช้"
            ) { _, _ ->

                val enteredPin =
                    pinInput.text.toString()

                if (enteredPin == savedPin) {

                    buildApp()

                } else {

                    Toast.makeText(
                        this@MainActivity,
                        "PIN ไม่ถูกต้อง",
                        Toast.LENGTH_SHORT
                    ).show()

                    showPin()
                }
            }

            dialog.show()
        }
    }

    // =========================================================
    // BUILD APP
    // =========================================================

    private fun buildApp() {

        val scroll =
            ScrollView(this)

        val page =
            LinearLayout(this)

        page.orientation =
            LinearLayout.VERTICAL

        page.setPadding(
            20,
            12,
            20,
            24
        )

        page.setBackgroundColor(
            Color.rgb(
                242,
                237,
                216
            )
        )

        scroll.addView(page)

        // =====================================================
        // HEADER
        // =====================================================

        val header =
            TextView(this)

        header.text =
            "🌿  THAI KINGDOM FRUIT"

        header.textSize =
            24f

        header.typeface =
            Typeface.DEFAULT_BOLD

        header.setTextColor(
            Color.WHITE
        )

        header.gravity =
            Gravity.CENTER

        header.setPadding(
            8,
            22,
            8,
            22
        )

        header.setBackgroundColor(
            Color.rgb(
                0,
                82,
                35
            )
        )

        page.addView(header)

        // =====================================================
        // DATE TIME
        // =====================================================

        addSection(
            page,
            "วันที่ / เวลา / LO / ครั้งที่"
        )

        val dateTime =
            TextView(this)

        val dateFormat =
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.US
            )

        val timeFormat =
            SimpleDateFormat(
                "HH:mm",
                Locale.US
            )

        val now =
            Date()

        dateTime.text =
            "วันที่ ${dateFormat.format(now)}    เวลา ${timeFormat.format(now)}"

        dateTime.textSize =
            16f

        dateTime.setPadding(
            8,
            10,
            8,
            10
        )

        page.addView(dateTime)

        // =====================================================
        // EXCHANGE RATE
        // =====================================================

        addSection(
            page,
            "อัตราแลกเปลี่ยน"
        )

        usd1 = field(
            page,
            "1-USD/THB",
            "33.90"
        )

        usd2 = field(
            page,
            "2-USD/THB",
            "32.00"
        )

        usd3 = field(
            page,
            "3-USD/THB",
            "31.00"
        )

        // =====================================================
        // PRODUCT DATA
        // =====================================================

        addSection(
            page,
            "ข้อมูลสินค้า"
        )

        kg = field(
            page,
            "KG",
            "00.00"
        )

        basket = field(
            page,
            "Basket",
            "00.00"
        )

        longanPrice = field(
            page,
            "ราคาลำไย/KG",
            "00.00"
        )

        packingPerKg = field(
            page,
            "Packing/KG",
            "00.00"
        )

        basketPrice = field(
            page,
            "ราคาตะกร้า",
            "00.00"
        )

        stickerRate = field(
            page,
            "อัตราค่าสติกเกอร์/Basket",
            "4.1863636364"
        )

        profitPerKg = field(
            page,
            "กำไร/KG",
            "00.00"
        )

        freightThb = field(
            page,
            "ค่าขนส่งต่อเที่ยว (THB)",
            "128184.00"
        )

        // =====================================================
        // RESULTS
        // =====================================================

        addSection(
            page,
            "ผลการคำนวณ"
        )

        longanCost =
            result(page, "ต้นทุนลำไย")

        packingCost =
            result(page, "Packing")

        basketCost =
            result(page, "ตะกร้า")

        stickerCost =
            result(page, "ค่าสติกเกอร์")

        profitCost =
            result(page, "กำไร")

        totalThb =
            result(page, "ผลลัพธ์ THB")

        totalUsd =
            result(page, "ผลลัพธ์ USD")

        freightUsd =
            result(page, "ค่าขนส่ง USD")

        summaryThb =
            result(page, "สรุป THB")

        summaryUsd =
            result(page, "สรุป USD")

        // =====================================================
        // NOTE
        // =====================================================

        addSection(
            page,
            "Note"
        )

        val note =
            EditText(this)

        note.hint =
            "บันทึกข้อความ"

        note.minLines =
            4

        note.gravity =
            Gravity.TOP

        note.setBackgroundColor(
            Color.WHITE
        )

        val noteParams =
            LinearLayout.LayoutParams(
                -1,
                150
            )

        noteParams.bottomMargin =
            12

        page.addView(
            note,
            noteParams
        )

        // =====================================================
        // CALCULATE BUTTON
        // =====================================================

        val calculateButton =
            Button(this)

        calculateButton.text =
            "คำนวณ"

        calculateButton.setOnClickListener {
            calculate()
        }

        page.addView(
            calculateButton
        )

        // =====================================================
        // CLEAR BUTTON
        // =====================================================

        val clearButton =
            Button(this)

        clearButton.text =
            "ล้างข้อมูล"

        clearButton.setOnClickListener {
            clearInputs()
        }

        page.addView(
            clearButton
        )

        // =====================================================
        // CHANGE PIN
        // =====================================================

        val pinButton =
            Button(this)

        pinButton.text =
            "เปลี่ยน PIN"

        pinButton.setOnClickListener {
            changePin()
        }

        page.addView(
            pinButton
        )

        setContentView(scroll)
    }

    // =========================================================
    // SECTION
    // =========================================================

    private fun addSection(
        parent: LinearLayout,
        title: String
    ) {

        val titleView =
            TextView(this)

        titleView.text =
            title

        titleView.textSize =
            19f

        titleView.typeface =
            Typeface.DEFAULT_BOLD

        titleView.setTextColor(
            Color.rgb(
                0,
                82,
                35
            )
        )

        titleView.setPadding(
            4,
            18,
            4,
            8
        )

        parent.addView(
            titleView
        )
    }

    // =========================================================
    // INPUT FIELD
    // =========================================================

    private fun field(
        parent: LinearLayout,
        label: String,
        initial: String
    ): EditText {

        val row =
            LinearLayout(this)

        row.orientation =
            LinearLayout.HORIZONTAL

        val labelView =
            TextView(this)

        labelView.text =
            label

        labelView.textSize =
            16f

        labelView.setPadding(
            4,
            12,
            4,
            12
        )

        val labelParams =
            LinearLayout.LayoutParams(
                0,
                -2,
                1f
            )

        row.addView(
            labelView,
            labelParams
        )

        val editText =
            EditText(this)

        editText.setText(
            initial
        )

        editText.textSize =
            17f

        editText.gravity =
            Gravity.CENTER

        editText.inputType =
            InputType.TYPE_CLASS_NUMBER or
            InputType.TYPE_NUMBER_FLAG_DECIMAL

        editText.selectAllOnFocus =
            true

        val inputParams =
            LinearLayout.LayoutParams(
                190,
                -2
            )

        row.addView(
            editText,
            inputParams
        )

        parent.addView(
            row
        )

        return editText
    }

    // =========================================================
    // RESULT FIELD
    // =========================================================

    private fun result(
        parent: LinearLayout,
        label: String
    ): TextView {

        val row =
            LinearLayout(this)

        row.orientation =
            LinearLayout.HORIZONTAL

        val labelView =
            TextView(this)

        labelView.text =
            label

        labelView.textSize =
            16f

        labelView.setPadding(
            4,
            12,
            4,
            12
        )

        val labelParams =
            LinearLayout.LayoutParams(
                0,
                -2,
                1f
            )

        row.addView(
            labelView,
            labelParams
        )

        val valueView =
            TextView(this)

        valueView.text =
            "0"

        valueView.textSize =
            17f

        valueView.gravity =
            Gravity.CENTER

        valueView.setPadding(
            8,
            12,
            8,
            12
        )

        valueView.setBackgroundColor(
            Color.WHITE
        )

        val valueParams =
            LinearLayout.LayoutParams(
                190,
                -2
            )

        row.addView(
            valueView,
            valueParams
        )

        parent.addView(
            row
        )

        return valueView
    }

    // =========================================================
    // NUMBER
    // =========================================================

    private fun num(
        editText: EditText
    ): Double {

        val text =
            editText.text.toString()

        val cleanText =
            text.replace(
                ",",
                ""
            )

        return cleanText.toDoubleOrNull()
            ?: 0.0
    }

    // =========================================================
    // CALCULATE
    // =========================================================

    private fun calculate() {

        val k =
            num(kg)

        val b =
            num(basket)

        val p =
            num(longanPrice)

        val pack =
            num(packingPerKg)

        val bp =
            num(basketPrice)

        val sr =
            num(stickerRate)

        val profit =
            num(profitPerKg)

        val u2 =
            num(usd2)

        val u3 =
            num(usd3)

        val freight =
            num(freightThb)

        // -----------------------------------------------------
        // สูตรเดิม
        // -----------------------------------------------------

        val cLongan =
            k * p

        val cPacking =
            k * pack

        val cBasket =
            b * bp

        val cSticker =
            b * sr

        val cProfit =
            k * profit

        val total =
            cLongan +
            cPacking +
            cBasket +
            cSticker +
            cProfit

        // -----------------------------------------------------
        // USD
        // -----------------------------------------------------

        val totalUsdValue =
            if (u2 != 0.0) {
                ceil(
                    total / u2
                ).toLong().toDouble()
            } else {
                0.0
            }

        // -----------------------------------------------------
        // FREIGHT USD
        // -----------------------------------------------------

        val freightUsdValue =
            if (u3 != 0.0) {
                ceil(
                    freight / u3
                ).toLong().toDouble()
            } else {
                0.0
            }

        // -----------------------------------------------------
        // SUMMARY
        // -----------------------------------------------------

        val summaryThbValue =
            total + freight

        val summaryUsdValue =
            totalUsdValue +
            freightUsdValue

        // -----------------------------------------------------
        // DISPLAY
        // -----------------------------------------------------

        longanCost.text =
            money(cLongan)

        packingCost.text =
            money(cPacking)

        basketCost.text =
            money(cBasket)

        stickerCost.text =
            money(cSticker)

        profitCost.text =
            money(cProfit)

        totalThb.text =
            money(total)

        totalUsd.text =
            money(totalUsdValue)

        freightUsd.text =
            money(freightUsdValue)

        summaryThb.text =
            money(summaryThbValue)

        summaryUsd.text =
            money(summaryUsdValue)
    }

    // =========================================================
    // CLEAR
    // =========================================================

    private fun clearInputs() {

        kg.setText("00.00")
        basket.setText("00.00")
        longanPrice.setText("00.00")
        packingPerKg.setText("00.00")
        basketPrice.setText("00.00")
        profitPerKg.setText("00.00")

        stickerRate.setText(
            "4.1863636364"
        )

        freightThb.setText(
            "128184.00"
        )

        calculate()
    }

    // =========================================================
    // CHANGE PIN
    // =========================================================

    private fun changePin() {

        val newPinInput =
            EditText(this)

        newPinInput.inputType =
            InputType.TYPE_CLASS_NUMBER or
            InputType.TYPE_NUMBER_VARIATION_PASSWORD

        newPinInput.hint =
            "PIN ใหม่"

        val dialog =
            AlertDialog.Builder(this)

        dialog.setTitle(
            "เปลี่ยน PIN"
        )

        dialog.setView(
            newPinInput
        )

        dialog.setPositiveButton(
            "บันทึก"
        ) { _, _ ->

            val newPin =
                newPinInput.text.toString()

            if (newPin.length >= 4) {

                val preferences =
                    getSharedPreferences(
                        "secure",
                        Context.MODE_PRIVATE
                    )

                val editor =
                    preferences.edit()

                editor.putString(
                    "pin",
                    newPin
                )

                editor.commit()

                Toast.makeText(
                    this@MainActivity,
                    "เปลี่ยน PIN สำเร็จ",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this@MainActivity,
                    "PIN ต้องมีอย่างน้อย 4 หลัก",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        dialog.setNegativeButton(
            "ยกเลิก",
            null
        )

        dialog.show()
    }
}