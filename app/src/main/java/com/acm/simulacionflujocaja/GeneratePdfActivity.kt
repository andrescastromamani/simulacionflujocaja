package com.acm.simulacionflujocaja

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_generate_pdf.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class GeneratePdfActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    val email=user?.email

    val flujoEfectivo = arrayOf("Flujo de efectivo proyectado por arctividades de Operacion", "Ingresos de operacion", "Gastos de operacion", "Flujo de efectivo por actividades de Operacion",
        "Ingresos de capital", "Gastos de capital", "Flujo de efectivo por actividades de financiamiento", "Fuentes", "Usos", "Incremento Proyectada del efectivo del periodo",
        "Efectivo al inicio del periodo", "Saldo de efecto final proyectado")
    val presupuestoCaja = arrayOf("ENTRADAS","ventas al contado","Recuperacion cuentas por cobrar 30 días","Recuperacion cuentas por cobrar 60 días","Total Entradas de efectivo",
        "SALIDAS DE EFECTIVO","Compras","Sueldos y Salarios","Aportes Patronales","Retroactivos Sueldos y salarios" ,"Retroactivos Aportes Patronales","Costos y Gastos","IVA","IT","IUE",
        "Otros impuestos","TOTAL SALIDAS","FLUJO NETO PROYECTADO SIN FINANCIAMIENTO","SALDO FINAL MES ANTERIOR","SALDO FINAL DE EFECTIVO PROYECTADO SIN FINANCIAMIENTO",
        "FINANCIAMIENTO A CORTO PLAZO","AMORTIZACION FINANCIAMIENTO A CORTO PLAZO","INTERESES FINANCIAMIENTO A CORTO PLAZO","SALDO FINAL DE EFECTIVO PROYECTADO CON FINANCIAMIENTO")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_pdf)
        btnExportarPdf.setOnClickListener{checkPermissions()}
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //permiso no aceptado
            requestDownloadPermission()
        }else{
            //Abrir Descarga
            openPdf()
        }
    }

    private fun openPdf() {
        db.collection("Users").document(email.toString()).collection("FlujoEfectivoProyectado").document("DatosFlujoProyectado").get().addOnSuccessListener {
            val flujoProyectadoActividadesOperacion = (it.get("flujoEfectivoActividadOperacion") as String?).toString()
            val ingresoOperacion = (it.get("ingresosOperacion") as String?).toString()
            val gastoOperacion = (it.get("gastosOperacion") as String?).toString()
            val flujoActividadesOperacion = (it.get("flujoActividadesInversion") as String?).toString()
            val ingresoCapital = (it.get("ingresosCapital") as String?).toString()
            val gastosCapital = (it.get("gastosCapital") as String?).toString()
            val flujoActividadesFinanciamiento = (it.get("flujoActividadesFinanciamiento") as String?).toString()
            val fuentes = (it.get("fuentes") as String?).toString()
            val usos = (it.get("usos") as String?).toString()
            val incrementoProyectadoEfectivoPeriodo = (it.get("incrementoEfectivoPeriodo") as String?).toString()
            val efectivoInicialPeriodo = (it.get("efectivoInicioPeriodo") as String?).toString()
            val saldoEfectivoFinalProyectado = (it.get("saldoEfectivoFinalProyectado") as String?).toString()
            val valuesFlujo = arrayOf(flujoProyectadoActividadesOperacion,ingresoOperacion,gastoOperacion,flujoActividadesOperacion,ingresoCapital,gastosCapital,
            flujoActividadesFinanciamiento,fuentes,usos,incrementoProyectadoEfectivoPeriodo,efectivoInicialPeriodo,saldoEfectivoFinalProyectado)


            Toast.makeText(this, "Reporte Descargado", Toast.LENGTH_LONG).show()
            val document = PdfDocument()
            val paint = Paint()

            //Page 1
            val pageInfo = PageInfo.Builder(250, 400, 1).create()
            val page: PdfDocument.Page = document.startPage(pageInfo)
            val canvas: Canvas = page.canvas

            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 12.0f
            canvas.drawText("REPORTE", (pageInfo.pageWidth / 2).toFloat(), 30F, paint)

            paint.textSize = 6.0f
            paint.color = Color.rgb(122, 119, 119)
            canvas.drawText(
                "Presupuesto de Caja y Flujo de Efectivo",
                (pageInfo.pageWidth / 2).toFloat(),
                40F,
                paint
            )

            paint.textAlign = Paint.Align.LEFT
            paint.textSize = 8.0f
            paint.color = Color.BLACK
            canvas.drawText("Flujo de Efectivo Proyectado", 10F, 70F, paint)

            paint.textAlign = Paint.Align.LEFT
            paint.textSize = 5f
            paint.color = Color.BLACK
            val startXPosition = 10
            val startXValuePosition = 180
            val endXPosition = pageInfo.pageWidth - 10
            var startYPosition = 100
            for (i in 0..11) {
                canvas.drawText(
                    flujoEfectivo[i],
                    startXPosition.toFloat(),
                    startYPosition.toFloat(),
                    paint
                )
                canvas.drawText(
                    valuesFlujo[i],
                    startXValuePosition.toFloat(),
                    startYPosition.toFloat(),
                    paint
                )
                canvas.drawLine(
                    startXPosition.toFloat(),
                    startYPosition.toFloat() + 3,
                    endXPosition.toFloat(),
                    startYPosition.toFloat() + 3,
                    paint
                )
                startYPosition += 20
            }
            canvas.drawLine(170F, 92F, 170F, 330F, paint)
            document.finishPage(page)

            //Page 2
            val pageInfo1 = PageInfo.Builder(250, 400, 1).create()
            val page1: PdfDocument.Page = document.startPage(pageInfo1)
            val canvas1: Canvas = page1.canvas

            paint.textAlign = Paint.Align.LEFT
            paint.textSize = 8.0f
            paint.color = Color.BLACK
            canvas1.drawText("Presupuesto de Caja", 10F, 30F, paint)

            paint.textAlign = Paint.Align.LEFT
            paint.textSize = 4f
            paint.color = Color.BLACK
            val startXPosition1 = 10
            val endXPosition1 = pageInfo.pageWidth - 10
            var startYPosition1 = 45
            for (i in 0..23) {
                canvas1.drawText(
                    presupuestoCaja[i],
                    startXPosition1.toFloat(),
                    startYPosition1.toFloat(),
                    paint
                )
                canvas1.drawLine(
                    startXPosition1.toFloat(),
                    startYPosition1.toFloat() + 3,
                    endXPosition1.toFloat(),
                    startYPosition1.toFloat() + 3,
                    paint
                )
                startYPosition1 += 15
            }
            canvas1.drawLine(140F, 40F, 140F, 395F, paint)
            canvas1.drawLine(175F, 40F, 175F, 395F, paint)
            canvas1.drawLine(210F, 40F, 210F, 395F, paint)

            document.finishPage(page1)


            val file = File(Environment.getExternalStorageDirectory(), "/simulacion.pdf")

            try {
                document.writeTo(FileOutputStream(file))
            } catch (e: IOException) {
                e.printStackTrace()
            }
            document.close()
        }
    }

    private fun requestDownloadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //El usuario rechazo los permisos
            Toast.makeText(this, "Permisos rechazados", Toast.LENGTH_SHORT).show()
        }else{
            //Pedir permisos
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),777)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 777){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openPdf()
            }else{
                Toast.makeText(this, "Permisos Rechazados por primera vez", Toast.LENGTH_SHORT).show()
            }
        }
    }
}