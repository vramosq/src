package com.ice.sgpr.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;

import com.ice.sgpr.SgprApplication;
import com.ice.sgpr.database.DatabaseHelper;
import com.ice.sgpr.database.TablaUsuariosTerceros;
import com.ice.sgpr.entidades.Parametro;

import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para los usuarios de terceros.
 *
 * @author eperaza
 *         Fecha de creacion: 08/06/2016.
 */
public class UsersTercerosDataAccess extends AbstractDataAccess {
    private String _sConsultaUsuarios = "SELECT " + TablaUsuariosTerceros.COL_USER_ID + ", " + TablaUsuariosTerceros.COL_USER_NAME
            + " FROM " + TablaUsuariosTerceros.NOMBRE_TABLA + " ORDER BY " + TablaUsuariosTerceros.COL_USER_NAME;

    public UsersTercerosDataAccess() {
        _helper = new DatabaseHelper(SgprApplication.getContext());
    }

    /**
     * Se insertan los usuarios en la tabla
     */
    public void insertarNuevosUsuarios(ArrayList<String[]> pUsersList) {
        borrarUsuarios();

        for(String[] user : pUsersList){
            ContentValues values = new ContentValues();
            values.put(TablaUsuariosTerceros.COL_USER_ID, user[0]);
            values.put(TablaUsuariosTerceros.COL_USER_NAME, user[1]);

            _database.insert(TablaUsuariosTerceros.NOMBRE_TABLA, null, values);
        }
    }

    /**
     * Retorna la lista con los usuarios
     * @return Lista de usuarios.
     */
    public List<Parametro> obtenerUsuariosTerceros() {
        Cursor cUsuario = _database.rawQuery(_sConsultaUsuarios, null);
        List<Parametro> lUsers = new ArrayList<>();

        if (cUsuario.moveToFirst()){
            //Se recorre el cursor, registro por registro.
            do {
                int nCodigo = cUsuario.getInt(0);
                String sNombre = cUsuario.getString(1);

                Parametro user = new Parametro(nCodigo, sNombre, "");
                lUsers.add(user);
            } while(cUsuario.moveToNext());
        }
        return lUsers;
    }

    /**
     * Borra los datos de la tabla.
     */
    public void borrarUsuarios() {
        _database.delete(TablaUsuariosTerceros.NOMBRE_TABLA, null, null);
    }
}
