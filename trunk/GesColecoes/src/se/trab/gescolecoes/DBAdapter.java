package se.trab.gescolecoes; 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	private static final String DATABASE_NAME = "collection";
	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_TAB_ITEM = "CREATE TABLE item ("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "tipo VARCHAR(255), "
			+ "titulo VARCHAR(255), " + "autor VARCHAR(255), "
			+ "editor VARCHAR(255), " + "ano_pub VARCHAR(8), "
			+ "edicao VARCHAR(255), " + "qrcode VARCHAR(1024), "
			+ "barcode VARCHAR(18), " + "ext_tipo VARCHAR(255), "
			+ "obs_pess VARCHAR(1024), " + "id_coll INTEGER " + ")";

	private static final String CREATE_TAB_COL = "CREATE TABLE col ("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "nomeCol VARCHAR(255), " + "local VARCHAR(255) " + ")";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TAB_ITEM);
			db.execSQL(CREATE_TAB_COL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS item");
			db.execSQL("DROP TABLE IF EXISTS col");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert a item into the database---
	public long insertItem(Item item) {

		ContentValues linha = new ContentValues();
		linha.put("tipo", item.tipo);
		linha.put("titulo", item.titulo);
		linha.put("autor", item.autor);
		linha.put("editor", item.editor);
		linha.put("ano_pub", item.ano_pub);
		linha.put("edicao", item.edicao);
		linha.put("qrcode", item.qrcode);
		linha.put("barcode", item.barcode);
		linha.put("ext_tipo", item.ext_tipo);
		linha.put("obs_pess", item.obs_pess);
		linha.put("id_coll", 0);
		return db.insert("item", null, linha);
	}

	// ---insert a collection into the database---
	public long insertCol(String nomeCol, String local) {

		ContentValues linha = new ContentValues();
		linha.put("nomeCol", nomeCol);
		linha.put("local", local);
		return db.insert("col", null, linha);
	}

	// ---deletes a particular item---
	public boolean deleteItem(int id) {
		return db.delete("item", "_id=" + id, null) > 0;
	}

	// ---deletes a particular collection---
	public boolean deleteCol(int id) {
		return db.delete("col", "_id=" + id, null) > 0;
	}

	// "title_raw like " + "'%Smith%'",

	// ---deletes a particular collection---
	public boolean deleteAllItems() {
		return db.delete("item", null, null) > 0;
	}

	// ---deletes a particular collection---
	public boolean deleteAllCols() {
		return db.delete("col", null, null) > 0;
	}

	// ---retrieves one item---
	// coluna : nome da coluna na BD
	// Valor : Valor a usar como filtro
	public Cursor getItem(String coluna, String Valor) throws SQLException {

		return db.query(true, "item", new String[] { "_id", "tipo", "titulo",
				"autor", "editor", "ano_pub", "edicao", "qrcode", "barcode",
				"ext_tipo", "obs_pess" }, coluna + "= ?", new String[] { Valor
				+ "%" }, null, null, null, null);
	}

	
	
	public Cursor getItemById(int id) {

		return db.query(true, "item",
				new String[] {  "_id", "tipo", "titulo",
				"autor", "editor", "ano_pub", "edicao", "qrcode", "barcode",
				"ext_tipo", "obs_pess", "id_coll" }, "_id="
						+ id, null, null, null, null, null);

	}
	
	

	// ---retrieves a collection---
	// coluna : nome da coluna na BD
	// Valor : Valor a usar como filtro
	public Cursor getCol(String coluna, String Valor) throws SQLException {
		return db.query(true, "col",
				new String[] { "_id", "nomeCol", "Local" }, coluna + "= ?",
				new String[] { Valor + "%" }, null, null, null, null);
	}

	// ---retrieves all items---
	public Cursor getItems() throws SQLException {

		return db.query(true, "item", new String[] { "_id", "tipo", "titulo",
				"autor", "editor", "ano_pub", "edicao", "qrcode", "barcode",
				"ext_tipo", "obs_pess" }, null, null, null, null, null, null);
	}

	// ---retrieves all collections---
	public Cursor getCols() throws SQLException {
		return db.query(true, "col",
				new String[] { "_id", "nomeCol", "Local" }, null, null, null,
				null, null, null);
	}
}
