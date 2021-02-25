package com.gcu.utility;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.gcu.model.Container;
import com.gcu.model.Image;
import com.gcu.model.User;

public class SqlFactory 
{
	public static String findQuery(Object obj) throws SQLException
	{
		String sql = "";
		
		if (obj.equals(Container.class))
		{
 			sql = "SELECT * FROM `CONTAINERS` " + 
					"LEFT JOIN `IMAGES` ON `CONTAINERS`.`C_ID` = `IMAGES`.`I_ID` " + 
					"WHERE `CONTAINERS`.`U_NAME` = (?)";
		}
		else if (obj.equals(Image.class))
		{
 			sql = "SELECT * FROM `IMAGES` WHERE `I_ID` = ?";
		}
		else if (obj.equals(User.class))
		{
 			sql = "SELECT * FROM `USERS` WHERE BINARY `U_NAME` = ? AND BINARY `U_PASSWORD` = ?";
		}
		
		return sql;
	}
	
	public static String findIfExistsQuery(Object obj) throws SQLException
	{
		String sql = "";
		
		if (obj.equals(Container.class))
		{
			sql = "SELECT * FROM `CONTAINERS` WHERE `U_NAME` = ? AND `I_ID` = ?";
		}
		else if (obj.equals(User.class))
		{
			sql = "SELECT * FROM `USERS` WHERE UPPER(`U_NAME`) LIKE UPPER(?)";
		}
		
		return sql;
	}
	
	public static String findAllQuery(Object obj) throws SQLException
	{
		String sql = "";
		
		if (obj.equals(Image.class))
		{
			sql = "SELECT * FROM `IMAGES` LIMIT 1000";
		}
		
		return sql;
	}
	
	/**
	 * Create a SQL Insert Query that corresponds with the object that was passed to the method.
	 * 
	 * @param obj Object that requires an insert query
	 * @return Returns the SQL query in the form of a String
	 * @throws SQLException
	 */
	public static String getSqlInsertQuery(Object obj) throws SQLException
	{
		String sql = "";
		
		// If object is of instance Container
		if(obj.equals(Container.class))
		{
			sql = "INSERT INTO `CONTAINERS` (C_NAME, C_DESCRIPTION, C_DOCKERID, U_NAME, I_ID) VALUES "
					+ "(?, ?, ?, ?, ?)";
		}
		// If object is of instance Image
		else if (obj.equals(Image.class))
		{
			sql = "INSERT INTO `IMAGES` "
					+ "(I_ID, I_INSTANCE, I_NAME, I_VERSION, I_PORT, I_TIER, I_CPU, I_RAM, I_STORAGE) VALUES "
					+ "(NULL, ?, ?, ?, ?, ?, ?, ?, ?)";
		}
		// If object is instance of User
		else if (obj.equals(User.class))
		{
			sql = "INSERT INTO `USERS` (U_NAME, U_PASSWORD) VALUES (?, ?)";
		}
		
		return sql;
	}
	
	/**
	 * Create a SQL Update Query that corresponds with the object that was passed to the method.
	 * 
	 * @param obj Object that requires an update query
	 * @return Returns the SQL query in the form of a String
	 * @throws SQLException
	 */
	public static String getSqlUpdateQuery(Object obj) throws SQLException
	{
		String sql = "";
		
		// If object is of instance Container
		if(obj.equals(Container.class))
		{
			sql = "UPDATE `CONTAINERS` SET C_DOCKERID = ? WHERE  U_NAME = ? AND I_ID = ?";
		}
		// If object is of instance Image
		else if (obj.equals(Image.class))
		{
			sql = "UPDATE `IMAGES` SET I_INSTANCE = ?, I_NAME = ?, I_VERSION = ?, I_PORT = ?, I_TIER = ?, I_CPU = ?, "
					+ "I_RAM = ?, I_STORAGE = ? WHERE I_ID = ?";
		}
		// If object is instance of User
		else if (obj.equals(User.class))
		{
			sql = "UPDATE `USERS` SET U_NAME = ?, U_PASSWORD = ? WHERE U_ID = ?";
		}
		
		return sql;
	}
	
	public static String getSqlDeleteQuery(Object obj) throws SQLException
	{
		String sql = "";
		
		return sql;
	}
	
	/**
	 * Gets an object from a SqlRowSet. This object corresponds to the object that was passed to the method
	 * 
	 * @param srs SqlRowSet that was passed to the method
	 * @param obj Object that was passed to the method
	 * @return Returns the object that was found in the SqlRowSet
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSqlRowSet(SqlRowSet srs, Object obj) throws SQLException
	{
		if (obj.equals(Container.class))
		{
			return (T) new Container(
					srs.getString("C_NAME"),
					srs.getString("C_DESCRIPTION"),
					srs.getString("C_DOCKERID"),
					srs.getString("U_NAME"),
					srs.getInt("I_ID")
					);
		}
		else if (obj.equals(Image.class))
		{
			return (T) new Image(
					srs.getInt("I_ID"),
					srs.getString("I_INSTANCE"),
					srs.getString("I_NAME"),
					srs.getString("I_VERSION"),
					srs.getInt("I_PORT"),
					srs.getString("I_TIER"),
					srs.getFloat("I_CPU"),
					srs.getBigDecimal("I_RAM"),
					srs.getInt("I_STORAGE")
					);
		}
		else if (obj.equals(User.class))
		{
			return (T) new User(
					srs.getString("U_NAME"),
					srs.getString("U_PASSWORD")
					);
		}
		
		return null;
	}
	
	/**
	 * Gets an object from a ResultSet. This object corresponds to the object that was passed to the method
	 * 
	 * @param rs ResultSet that was passed to the method
	 * @param obj Object that was passed to the method
	 * @return Returns the object that was found in the ResultSet
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getResultSet(ResultSet rs, Object obj) throws SQLException
	{
		if (obj.equals(Container.class))
		{
			return (T) new Container(
					rs.getString("C_NAME"),
					rs.getString("C_DESCRIPTION"),
					rs.getString("C_DOCKERID"),
					rs.getString("U_NAME"),
					rs.getInt("I_ID")
					);
		}
		else if (obj.equals(Image.class))
		{
			return (T) new Image(
					rs.getInt("I_ID"),
					rs.getString("I_INSTANCE"),
					rs.getString("I_NAME"),
					rs.getString("I_VERSION"),
					rs.getInt("I_PORT"),
					rs.getString("I_TIER"),
					rs.getFloat("I_CPU"),
					rs.getBigDecimal("I_RAM"),
					rs.getInt("I_STORAGE")
					);
		}
		else if (obj.equals(User.class))
		{
			return (T) new User(
					rs.getString("U_NAME"),
					rs.getString("U_PASSWORD")
					);
		}
		
		return null;
	}

}
