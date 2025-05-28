package com.example.demokat.database;

import static com.example.demokat.controllers.MainActivity.user_id;

import android.content.Context;
import android.widget.Toast;

import com.example.demokat.R;
import com.example.demokat.models.RecModel;
import com.example.demokat.models.UsuarioModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DAO extends DatabaseManager{

        protected PreparedStatement sentenciaPar = null;
        protected PreparedStatement sentenciaPar2 = null;
        protected DatabaseManager conexion = new DatabaseManager();
        protected Connection connection;
        protected String query;
        protected String query2;


        public DAO() throws SQLException {
            this.connection = conexion.connect();
        }


    public UsuarioModel selectUserData(UsuarioModel usuario, Context context) {
        String userName = usuario.getUser();
        String passwd = usuario.getContrasena();

        String query = "SELECT * FROM usuario WHERE username = ? AND contrasena = ?;";
        try {

            PreparedStatement sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1, userName);
            sentenciaPar.setString(2, passwd);


            ResultSet resultado = sentenciaPar.executeQuery();
            if (resultado.next()) {

                return new UsuarioModel(resultado.getInt("id_user"), resultado.getString("username"));
            }
            else {
                Toast.makeText(context, R.string.incorrect, Toast.LENGTH_SHORT).show();
                return new UsuarioModel("erroruser");
            }




        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.error_bd, Toast.LENGTH_SHORT).show();
            return new UsuarioModel("errordb");
        }

    }


    public String[] selectTitulosRec(int user_id, String ins) {
        String query = "SELECT titulo FROM rec WHERE user_id =? AND instrumento =?   ORDER BY fecha ";
        ArrayList<String> titulos = new ArrayList<>();

        try {

            PreparedStatement sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setInt(1, user_id);
            sentenciaPar.setString(2, ins);
            ResultSet resultSet = sentenciaPar.executeQuery();
            while (resultSet.next()) {

                titulos.add(resultSet.getString("titulo"));
            }

            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    public String[] selectAllTitulosRecCanciones(String titulo, int user_id) {
        String query = "SELECT rec.titulo\n" +
                "FROM rec\n" +
                "JOIN cancion_rec ON rec.id_rec = cancion_rec.rec_id\n" +
                "JOIN cancion ON cancion.id_cancion = cancion_rec.cancion_id\n" +
                "WHERE cancion.titulo = ?\n " +
                "AND cancion.user_id = ?;\n ";
        ArrayList<String> titulos = new ArrayList<>();

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setInt(2, user_id);
            sentenciaPar.setString(1,titulo);
            ResultSet resultSet = sentenciaPar.executeQuery();

            while (resultSet.next()) {

                titulos.add(resultSet.getString("titulo"));
            }

            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    public String[] searchTitulosRecCanciones(String tituloCancion, int user_id, String tituloRec) {
        String query = "SELECT rec.titulo\n" +
                "FROM rec\n" +
                "JOIN cancion_rec ON rec.id_rec = cancion_rec.rec_id\n" +
                "JOIN cancion ON cancion.id_cancion = cancion_rec.cancion_id\n" +
                "WHERE cancion.titulo = ?\n " +
                "AND cancion.user_id = ?\n" +
                "AND rec.titulo LIKE ?;\n ";
        ArrayList<String> titulos = new ArrayList<>();

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setInt(2, user_id);
            sentenciaPar.setString(1,tituloCancion);
            sentenciaPar.setString(3,"%" + tituloRec + "%");
            ResultSet resultSet = sentenciaPar.executeQuery();

            while (resultSet.next()) {

                titulos.add(resultSet.getString("titulo"));
            }

            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    public String[] selectAllInstrumentosCanciones(String titulo, int user_id) {
        String query = "SELECT rec.instrumento\n" +
                "FROM rec\n" +
                "JOIN cancion_rec ON rec.id_rec = cancion_rec.rec_id\n" +
                "JOIN cancion ON cancion.id_cancion = cancion_rec.cancion_id\n" +
                "WHERE cancion.titulo = ?\n " +
                "AND cancion.user_id = ?;\n ";
        ArrayList<String> titulos = new ArrayList<>();

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1, titulo);
            sentenciaPar.setInt(2, user_id);

            ResultSet resultSet = sentenciaPar.executeQuery();

            while (resultSet.next()) {

                titulos.add(resultSet.getString("instrumento"));
            }

            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] searchInstrumentosCanciones(String tituloCan, int user_id, String tituloRec) {
        String query = "SELECT rec.instrumento\n" +
                "FROM rec\n" +
                "JOIN cancion_rec ON rec.id_rec = cancion_rec.rec_id\n" +
                "JOIN cancion ON cancion.id_cancion = cancion_rec.cancion_id\n" +
                "WHERE cancion.titulo = ?\n " +
                "AND cancion.user_id = ?\n" +
                "AND rec.titulo LIKE ?;\n ";
        ArrayList<String> titulos = new ArrayList<>();

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1, tituloCan);
            sentenciaPar.setString(3, "%" + tituloRec + "%");
            sentenciaPar.setInt(2, user_id);

            ResultSet resultSet = sentenciaPar.executeQuery();

            while (resultSet.next()) {

                titulos.add(resultSet.getString("instrumento"));
            }

            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] selectAllTitulosCanciones(int user_id) {
        String query = "SELECT titulo FROM cancion WHERE user_id =?";
        ArrayList<String> titulos = new ArrayList<>();

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setInt(1, user_id);
            ResultSet resultSet = sentenciaPar.executeQuery();

            while (resultSet.next()) {

                titulos.add(resultSet.getString("titulo"));
            }

            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

   public String[] selectAllTitulosNotasCancion(String titulo, int user_id) {
        String query = "SELECT notas.titulo\n" +
                "FROM notas\n" +
                "JOIN cancion_rec ON notas.id_notas = cancion_rec.notas_id\n" +
                "JOIN cancion ON cancion.id_cancion = cancion_rec.cancion_id\n" +
                "WHERE cancion.titulo = ? \n" +
                "AND cancion.user_id = ?;\n";
        ArrayList<String> titulos = new ArrayList<>();

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setString(1, titulo);
            sentenciaPar.setInt(2, user_id);
            ResultSet resultSet = sentenciaPar.executeQuery();

            while (resultSet.next()) {

                titulos.add(resultSet.getString("titulo"));
            }

            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String[] searchTitulosNotasCancion(String tituloCan, int user_id, String tituloNotas) {
        String query = "SELECT notas.titulo\n" +
                "FROM notas\n" +
                "JOIN cancion_rec ON notas.id_notas = cancion_rec.notas_id\n" +
                "JOIN cancion ON cancion.id_cancion = cancion_rec.cancion_id\n" +
                "WHERE cancion.titulo = ? \n" +
                "AND cancion.user_id = ?\n" +
                "AND notas.titulo LIKE ?;\n";
        ArrayList<String> titulos = new ArrayList<>();

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setString(1, tituloCan);
            sentenciaPar.setString(3, "%" + tituloNotas + "%");
            sentenciaPar.setInt(2, user_id);
            ResultSet resultSet = sentenciaPar.executeQuery();

            while (resultSet.next()) {

                titulos.add(resultSet.getString("titulo"));
            }

            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String[] selectTitulosNotas(int user_id) {
        String query = "SELECT titulo FROM notas WHERE user_id =?";
        ArrayList<String> titulos = new ArrayList<>();

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setInt(1, user_id);
            ResultSet resultSet = sentenciaPar.executeQuery();

            while (resultSet.next()) {

                titulos.add(resultSet.getString("titulo"));
            }

            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String loadURI(int user_id, String instrumento, String titulo){

        String query = "SELECT rec FROM rec WHERE user_id = ? AND instrumento = ? AND titulo= ?;";
        try {

            PreparedStatement sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setInt(1, user_id);
            sentenciaPar.setString(2, instrumento);
            sentenciaPar.setString(3, titulo);


            ResultSet resultado = sentenciaPar.executeQuery();
            if (resultado.next()) {

                return resultado.getString("rec");
            }





        } catch (SQLException e) {
            e.printStackTrace();

        }

        return null;
    }


    public void insertRec(RecModel recModel) {
            String rec = recModel.getRec();
            String titulo = recModel.getTitulo();
            String instrumento = recModel.getInstrumento();
            Timestamp fecha = recModel.getFecha();
            int user_id = recModel.getUser_id();

            try {

            query ="INSERT INTO rec (rec, titulo, instrumento,fecha, user_id) VALUES (?,?,?,?,?);";



                sentenciaPar = connection.prepareStatement(query);

                sentenciaPar.setString(1,rec);
                sentenciaPar.setString(2,titulo);
                sentenciaPar.setString(3,instrumento);
                sentenciaPar.setTimestamp(4, fecha);
                sentenciaPar.setInt(5,user_id);




                sentenciaPar.executeUpdate();

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            }


    public int checkTitle(int user_id, String ins, String tituloEdit) {
        String query = "SELECT id_rec FROM rec WHERE user_id =? AND instrumento =? AND titulo = ? ";

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setInt(1, user_id);
            sentenciaPar.setString(2, ins);
            sentenciaPar.setString(3, tituloEdit);
            ResultSet resultSet = sentenciaPar.executeQuery();

            if(resultSet.next()) {

                return 0;
            }

            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return 2;
        }
    }

    public int checkCancion(int user_id, String tituloEdit) {
        String query = "SELECT id_cancion FROM cancion WHERE user_id =? AND titulo = ? ";

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setInt(1, user_id);
            sentenciaPar.setString(2, tituloEdit);
            ResultSet resultSet = sentenciaPar.executeQuery();

            if(resultSet.next()) {

                return 0;
            }

            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return 2;
        }
    }

    public int checkTitleNotas(int user_id, String tituloEdit) {
        String query = "SELECT id_notas FROM notas WHERE user_id =? AND titulo = ? ";

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setInt(1, user_id);
            sentenciaPar.setString(2, tituloEdit);
            ResultSet resultSet = sentenciaPar.executeQuery();

            if(resultSet.next()) {

                return 0;
            }

            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return 2;
        }
    }



    public String[] searchTitulosRec(int user_id, String ins, String search) {

        try {
            String query = "SELECT titulo FROM rec WHERE user_id = ? AND instrumento = ? AND titulo LIKE ?";
            ArrayList<String> titulos = new ArrayList<>();
            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setInt(1, user_id);
            sentenciaPar.setString(2, ins);
            sentenciaPar.setString(3, "%" + search + "%");

            ResultSet resultSet = sentenciaPar.executeQuery();
            while (resultSet.next()) {
                titulos.add(resultSet.getString("titulo"));
            }
            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] searchTitulosNotas(int user_id, String search) {

        try {
            String query = "SELECT titulo FROM notas WHERE user_id = ? AND titulo LIKE ?";
            ArrayList<String> titulos = new ArrayList<>();
            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setInt(1, user_id);
            sentenciaPar.setString(2, "%" + search + "%");

            ResultSet resultSet = sentenciaPar.executeQuery();
            while (resultSet.next()) {
                titulos.add(resultSet.getString("titulo"));
            }
            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] searchTitulosCanciones(int user_id, String search) {

        try {
            String query = "SELECT titulo FROM cancion WHERE user_id = ? AND titulo LIKE ?";
            ArrayList<String> titulos = new ArrayList<>();
            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setInt(1, user_id);
            sentenciaPar.setString(2, "%" + search + "%");

            ResultSet resultSet = sentenciaPar.executeQuery();
            while (resultSet.next()) {
                titulos.add(resultSet.getString("titulo"));
            }
            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    public String[] searchTitulosCancionesRec(int user_id, String search) {

        try {
            String query = "SELECT titulo FROM cancion WHERE user_id = ? AND titulo LIKE ?";
            ArrayList<String> titulos = new ArrayList<>();
            PreparedStatement sentenciaPar = connection.prepareStatement(query);

            sentenciaPar.setInt(1, user_id);
            sentenciaPar.setString(2, "%" + search + "%");

            ResultSet resultSet = sentenciaPar.executeQuery();
            while (resultSet.next()) {
                titulos.add(resultSet.getString("titulo"));
            }
            return titulos.toArray(new String[0]);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }





    public Timestamp checkFecha(String title, String instrumento, int userid) {
        String query = "SELECT fecha FROM rec WHERE titulo = ? AND instrumento =? AND user_id=?;";

        try {


            PreparedStatement sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1,title);
            sentenciaPar.setString(2,instrumento);
            sentenciaPar.setInt(3,userid);

            ResultSet resultSet = sentenciaPar.executeQuery();

            if(resultSet.next()) {

                return resultSet.getTimestamp("fecha");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }



    public void editTitle(int userId, String ins, String tituloEdit, String tituloOg) {


        try {

            query ="UPDATE rec SET titulo = ? WHERE instrumento = ? AND user_id = ? AND titulo = ?;";
            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1,tituloEdit);
            sentenciaPar.setString(2,ins);
            sentenciaPar.setInt(3,userId);
            sentenciaPar.setString(4,tituloOg);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void editTitleNotas(int userId, String tituloEdit, String tituloOg) {


        try {

            query ="UPDATE notas SET titulo = ? WHERE user_id = ? AND titulo = ?;";
            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1,tituloEdit);
            sentenciaPar.setInt(2,userId);
            sentenciaPar.setString(3,tituloOg);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void editNotas(int userId, String notas, String tituloOg) {


        try {

            query ="UPDATE notas SET notas = ? WHERE user_id = ? AND titulo = ?;";
            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1,notas);
            sentenciaPar.setInt(2,userId);
            sentenciaPar.setString(3,tituloOg);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    public void deleteRec(int userId, String ins, String titulo) {


        try {

            query ="DELETE FROM rec WHERE instrumento = ? AND user_id = ? AND titulo = ?;";
            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1,ins);
            sentenciaPar.setInt(2,userId);
            sentenciaPar.setString(3,titulo);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }





    public void insertNota(String titulo, String notas, int user_id) {
        try {

            query ="INSERT INTO notas (notas, titulo, user_id) VALUES (?,?,?);";
            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(2,titulo);
            sentenciaPar.setString(1,notas);
            sentenciaPar.setInt(3,user_id);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }


    public void insertCancion(String titulo, int user_id) {
        try {

            query ="INSERT INTO cancion  (titulo, user_id) VALUES (?,?);";
            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1,titulo);
            sentenciaPar.setInt(2,user_id);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public void insertRecCancion(String tituloCancion, String tituloRec, int user_id, String instrumento) {
        try {

            query ="INSERT INTO cancion_rec (rec_id, cancion_id)\n" +
                    "SELECT rec.id_rec, cancion.id_cancion\n" +
                    "FROM rec\n" +
                    "JOIN cancion ON cancion.titulo = ? AND cancion.user_id = ?\n" +
                    "WHERE rec.titulo = ? AND rec.instrumento = ?;\n";



            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1,tituloCancion);
            sentenciaPar.setInt(2,user_id);
            sentenciaPar.setString(3,tituloRec);
            sentenciaPar.setString(4,instrumento);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }


    public void insertNotasCancion(String tituloCancion, String tituloRec, int user_id) {
        try {

            query ="INSERT INTO cancion_rec (notas_id, cancion_id)\n" +
                    "SELECT notas.id_notas, cancion.id_cancion\n" +
                    "FROM notas\n" +
                    "JOIN cancion ON cancion.titulo = ? AND cancion.user_id = ?\n" +
                    "WHERE notas.titulo = ?;\n";



            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1,tituloCancion);
            sentenciaPar.setInt(2,user_id);
            sentenciaPar.setString(3,tituloRec);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }


    public void deleteRecCancion(String tituloCancion, String tituloRec, int user_id, String instrumento) {
        try {

            query = "DELETE FROM cancion_rec\n" +
                    "WHERE rec_id = (\n" +
                    "SELECT rec.id_rec\n" +
                    "FROM rec\n" +
                    " WHERE rec.titulo = ?\n" +
                    "AND rec.instrumento = ?\n"+
                    ") AND cancion_id = (\n" +
                    "SELECT cancion.id_cancion\n" +
                    "FROM cancion\n" +
                    "WHERE cancion.titulo = ? AND cancion.user_id = ?\n" +
                    ");";




            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(1,tituloRec);
            sentenciaPar.setString(2,instrumento);
            sentenciaPar.setString(3,tituloCancion);
            sentenciaPar.setInt(4,user_id);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }



    public void deleteNotasCancion(String tituloCancion, String tituloRec, int user_id) {
        try {

            query = "DELETE FROM cancion_rec\n" +
                    "WHERE notas_id = (\n" +
                    "    SELECT notas.id_notas\n" +
                    "    FROM notas\n" +
                    "    WHERE notas.titulo = ?\n" +
                    ") AND cancion_id = (\n" +
                    "    SELECT cancion.id_cancion\n" +
                    "    FROM cancion\n" +
                    "    WHERE cancion.titulo = ? AND cancion.user_id = ?\n" +
                    ");\n";

            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(2,tituloCancion);
            sentenciaPar.setInt(3,user_id);
            sentenciaPar.setString(1,tituloRec);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public String checkRecCancion(String tituloRec, String instrumento, int user_id) {
        try {

            query = "SELECT cancion.titulo\n" +
                    "FROM cancion\n" +
                    "JOIN cancion_rec ON cancion.id_cancion = cancion_rec.cancion_id\n" +
                    "JOIN rec ON rec.id_rec = cancion_rec.rec_id\n" +
                    "WHERE rec.titulo = ?\n" +
                    "  AND rec.instrumento = ?\n" +
                    "  AND rec.user_id = ?;\n";

            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setString(2,instrumento);
            sentenciaPar.setInt(3,user_id);
            sentenciaPar.setString(1,tituloRec);

           ResultSet rs= sentenciaPar.executeQuery();
           if(rs.next()){
               return rs.getString("titulo");
           }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return "";
    }


    public String checkNotasCancion(String tituloRec, int user_id) {
        try {

            query = "SELECT cancion.titulo\n" +
                    "FROM cancion\n" +
                    "JOIN cancion_rec ON cancion.id_cancion = cancion_rec.cancion_id\n" +
                    "JOIN notas ON notas.id_notas = cancion_rec.notas_id\n" +
                    "WHERE notas.titulo = ?\n" +
                    "  AND notas.user_id = ?;\n";

            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setInt(2,user_id);
            sentenciaPar.setString(1,tituloRec);

            ResultSet rs= sentenciaPar.executeQuery();
            if(rs.next()){
                return rs.getString("titulo");
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return "";
    }


    public String loadNota(int user_id,String titulo){
        try {

            query ="SELECT notas FROM notas WHERE user_id = ? AND titulo = ?;";
            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setInt(1,user_id);
            sentenciaPar.setString(2,titulo);
            ResultSet resultSet = sentenciaPar.executeQuery();
            if(resultSet.next()) {
                return resultSet.getString("notas");
            }


        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return "error";
    }

    public void deleteNota(int userId, String titulo) {

        try {

            query ="DELETE FROM notas WHERE user_id = ? AND titulo = ?;";
            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setInt(1,userId);
            sentenciaPar.setString(2,titulo);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    public void deleteCancion(int userId, String titulo) {


        try {

            query ="DELETE FROM cancion WHERE user_id = ? AND titulo = ?;";
            sentenciaPar = connection.prepareStatement(query);
            sentenciaPar.setInt(1,userId);
            sentenciaPar.setString(2,titulo);
            sentenciaPar.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void insertUser(String username, String name, String surname, String password){
        query ="INSERT INTO usuario (username, name, surname, contrasena) VALUES (?,?,?,?)";
        try {
            sentenciaPar = connection.prepareStatement(query);
        sentenciaPar.setString(1,username);
        sentenciaPar.setString(2,name);
        sentenciaPar.setString(3,surname);
        sentenciaPar.setString(4,password);
        sentenciaPar.executeUpdate();

    } catch (SQLException ex) {
        throw new RuntimeException(ex);
    }
    }



}















