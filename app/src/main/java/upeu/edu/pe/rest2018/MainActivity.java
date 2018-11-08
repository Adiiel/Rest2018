package upeu.edu.pe.rest2018;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private EditText edtnom, edtape,edtus,edtcla;
    private ListView lvdatos;
    private Button boton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtnom = (EditText)findViewById(R.id.nombres);
        edtape=(EditText)findViewById(R.id.apellidos);
        edtus =(EditText)findViewById(R.id.usuario);
        edtcla=(EditText)findViewById(R.id.clave);
        boton1 = (Button) findViewById(R.id.btncalcular);
    }

    public void save(View view) {
        String nombres = edtnom.getText().toString();
        String apellidos= edtape.getText().toString();
        String usuario = edtus.getText().toString();
        String clave = edtcla.getText().toString();
        datosUsuarios(nombres,apellidos,usuario,clave);
    }

    public void datosUsuarios(String nombres, String apellidos, String usuario, String clave) {
        AsyncHttpClient client = new AsyncHttpClient();
        Toast.makeText(MainActivity.this, "SI", Toast.LENGTH_SHORT).show();
        String url = "http://172.17.8.34/rest/registro.php?";
        String parametros = "Nombres=" + nombres + "&Apellidos=" + apellidos + "&Usuario=" + usuario + "&Clave=" + clave;
        //http://172.17.8.34/rest/registro.php?Nombres=hola&Apellidos=hola2&Usuario=abc&Clave=123
        client.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String resultado = new String(responseBody);
                    Toast.makeText(MainActivity.this, "Ok: " + resultado, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "piña", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Mal: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void cargarLisview(ArrayList<String> datos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        lvdatos.setAdapter(adapter);
    }

    public void listarUsuarios() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://172.17.8.34/rest/ListaUsuarios.php";
        RequestParams params = new RequestParams();
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    cargarLisview(getJson(new String(responseBody)));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public ArrayList<String> getJson(String response) {
        ArrayList<String> lista = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            String cadena;
            for (int i = 0; i < array.length(); i++) {
                cadena = array.getJSONObject(i).getString("usuario");
                lista.add(cadena);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
