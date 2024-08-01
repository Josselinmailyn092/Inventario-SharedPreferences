package com.example.persistencia_datos;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    //Creación de Objetos
    private EditText edt1, edt2, edt3, edt4, edt5, edt6,edt7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicialización de objetos, vincularlos con los elementos de la GUI.
        edt1= findViewById(R.id.nombre_producto);
        edt2 = findViewById(R.id.fecha);
        edt3= findViewById(R.id.cantidad);
        edt4= findViewById(R.id.precio_unitario);
        edt5= findViewById(R.id.stock);
        edt6= findViewById(R.id.proveedor);
        edt7 = findViewById(R.id.multilinea);


        //Crear archivo SharedPreferences para guardar datos
        SharedPreferences sp = getSharedPreferences("Datos_Productos", Context.MODE_PRIVATE);
        // Cargar datos almacenados en SharedPreferences al ingresar en la aplicación.
        //cargarDatos();
    }

    // Método para guardar datos
    public void guardarDatos(View view) {
        //Realizar validaciones antes de guardar --->  validarDatos
        if (!validarDatos()) {
            return;
        }
        //Obtener valores en el campo de entrada
        String nombreProducto = edt1.getText().toString().trim();
        String fecha = edt2.getText().toString();
        String cantidad= edt3.getText().toString();
        String precio =edt4.getText().toString();
        String stock= edt5.getText().toString();
        String proveedor= edt6.getText().toString();
        String producto = nombreProducto + ";"+ fecha + ";" + cantidad + ";" +precio+ ";" +stock + ";"+proveedor;

        //Instanciar SharedPreferences
        SharedPreferences sp = getSharedPreferences("Datos_Productos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //Guardar la cadena con putString
        editor.putString(nombreProducto, producto);
        editor.apply(); //aplicar cambios

        //Mensaje Toast
        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
    }


    // Método para buscar datos
    public void buscarDatos(View view) {
        //Obtener el nombre del producto del multiline
        String nombreProducto = edt7.getText().toString().trim();

        //Validación en caso de que el campo este vacio,indicando en un toast
        // que se debe ingresar el nombre del producto a buscar en el toast
        if (nombreProducto.isEmpty()){
            Toast.makeText(this, "Por favor ingresa el nombre del " +
                    "producto en el campo a Multiline", Toast.LENGTH_SHORT).show();
            return;
        }

        //Verificar si el nombre del producto ingresado coincide con los datos guardados
        //trim elimina espacios en blanco
        String nombreBuscarProducto = edt7.getText().toString().trim();
        if (!nombreProducto.equals(nombreBuscarProducto)){
            Toast.makeText(this, "El nombre ingresado no esta almacenado",
                    Toast.LENGTH_SHORT).show();//si no se encuentra devuelve un mensaje en un toast
            return;
        }
        //Obtener datos guardados --> como  clave es el nombre del producto
        SharedPreferences sp = getSharedPreferences("Datos_Productos", Context.MODE_PRIVATE);
        String producto = sp.getString(nombreProducto, null);
        //En caso de existir descompone los datos usando split separados por ";"
        if(producto != null){
            String [] pr = producto.split(";");
            String nombreProd = pr [0];
            String fecha = pr [1];
            String cantidad = pr [2];
            String precioUnitario = pr [3];
            String stock = pr[4];
            String proveedor = pr[5];

            // Completar los campos con los datos encontrados
            edt1.setText(nombreProd);
            edt2.setText(fecha);
            edt3.setText(cantidad);
            edt4.setText(precioUnitario);
            edt5.setText(stock);
            edt6.setText(proveedor);

            Toast.makeText(this, "Datos cargados", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para limpiar campos
    public void limpiarCampos(View view) {
        edt1.setText("");
        edt2.setText("");
        edt3.setText("");
        edt4.setText("");
        edt5.setText("");
        edt6.setText("");
        edt7.setText("");

        Toast.makeText(this, "Campos limpiados", Toast.LENGTH_SHORT).show();
    }
    // Método para validar datos
    private boolean validarDatos() {
        String producto = edt1.getText().toString();
        String fecha = edt2.getText().toString();
        String cantidadStr = edt3.getText().toString();
        String precioStr = edt4.getText().toString();
        String stockStr = edt5.getText().toString();
        String proveedor = edt6.getText().toString();


        //Validar Nombre de prodicto que no este vacio
        if (producto.trim().isEmpty()) {
            Toast.makeText(this, "Ingrese nombre de producto, no puede estar vacío!!!!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar fecha (ejemplo: dd/MM/yyyy)
        if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
            Toast.makeText(this, "Fecha inválida (debe ser dd/MM/yyyy)", Toast.LENGTH_SHORT).show();
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);

        try {
            // Parsear la fecha ingresada
            Date fechaIngresada = dateFormat.parse(fecha);

            // Obtener la fecha actual
            Date fechaActual = Calendar.getInstance().getTime();

            // Comparar las fechas
            if (fechaIngresada.after(fechaActual)) {
                Toast.makeText(this, "Fecha inválida (no puede ser mayor a la fecha actual)", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (ParseException e) {
            // Si la fecha no es válida
            Toast.makeText(this, "Fecha inválida", Toast.LENGTH_SHORT).show();
            return false;
        }


        // Validar cantidad (solo números mayores a 0)
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                Toast.makeText(this, "Cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar precio unitario (debe ser un float válido)
        try {
            float precio = Float.parseFloat(precioStr);
            if (precio <= 0) {
                Toast.makeText(this, "Precio unitario debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio unitario inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar stock (números enteros positivos)
        int stock;
        try {
            stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                Toast.makeText(this, "Stock debe ser un número entero positivo", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Stock inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar proveedor (no vacío)
        if (proveedor.trim().isEmpty()) {
            Toast.makeText(this, "Proveedor no puede estar vacío!!!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}












