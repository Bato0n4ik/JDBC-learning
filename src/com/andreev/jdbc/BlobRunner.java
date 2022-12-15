package com.andreev.jdbc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobRunner {

    public static void main(String[] args){
        //imageLoader();

        try{
            showImage();
        }
        finally{
            ConnectionManager.closePool();
        }
    }

    private static void imageLoader(){
        String sql = """
                UPDATE aircraft
                SET image = ?
                WHERE id = 1;
                """;
        try(Connection connection = ConnectionManager.get();
        var statement = connection.prepareStatement(sql)){

            statement.setBytes(1, Files.readAllBytes(Path.of("resources", "boeing 777-300.jpg")));
            statement.executeUpdate();

        }
        catch(SQLException | IOException exc){
            throw new RuntimeException(exc);
        }
    }

    private static void showImage(){
        String sql = """
                SELECT image
                FROM aircraft
                WHERE id = 1;
                """;
        try(Connection connection = ConnectionManager.get();
            var statement = connection.prepareStatement(sql)){

            byte[] blob = Files.readAllBytes(Path.of("E:\\IdeaProjects\\jdbc-starter\\resources\\boeing 777-300.jpg"));
            ResultSet result = statement.executeQuery();
            if(result.next()){
                var byteImage = result.getBytes("image");
                Files.write(Path.of("resources", "boeing-777-300.jpg"), byteImage, StandardOpenOption.CREATE);
            }
        }
        catch(Exception exc){
            throw new RuntimeException(exc);
        }
    }
}
