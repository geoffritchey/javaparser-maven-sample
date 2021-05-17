package com.ritchey.maven_sample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;

/**
 * Some code that uses JavaParser.
 */
public class LogicPositivizer {
	private static final String FILENAME = "src/main/resources/hibernate/Address.hbm.xml";

	public static void main(String[] args) {
		// JavaParser has a minimal logging class that normally logs nothing.
		// Let's ask it to write to standard out:
		Log.setAdapter(new Log.StandardOutStandardErrorAdapter());

		// SourceRoot is a tool that read and writes Java files from packages on a
		// certain root directory.
		// In this case the root directory is found by taking the root from the current
		// Maven module,
		// with src/main/resources appended.
		SourceRoot sourceRoot = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

		db.setErrorHandler(new DefaultHandler());

		File directoryPath = new File("src/main/resources/hibernate");

		File filesList[] = directoryPath.listFiles((file) -> file.getPath().endsWith(".xml"));

		for (File file : filesList) {
			Log.info("filename = " + file.getName());
			sourceRoot = new SourceRoot(
					CodeGenerationUtils.mavenModuleRoot(LogicPositivizer.class).resolve("src/main/resources"));

			Document doc = null;
			try {
				doc = db.parse(file);
			} catch (SAXException | IOException e) {
				e.printStackTrace();
			}

			Element root = (Element) doc.getElementsByTagName("hibernate-mapping").item(0);
			Mapping mapping = (Mapping) ParseFactory.Create(root);
			Clazz clazz = mapping.clazz.get(0);
			String[] names = clazz.name.split("\\.");
			String packageName = mapping.packageName;
			if (packageName == null || "".equals(packageName))
				packageName = String.join(".", Arrays.copyOf(names, names.length - 1));

			// Our sample is in the root of this directory, so no package name.
			CompilationUnit cu = sourceRoot.parse(packageName, names[names.length - 1] + ".java");

			Log.info("Positivizing!");

			cu.accept(new ModifierVisitor<Void>() {
				@Override
				public Visitable visit(final ClassOrInterfaceDeclaration n, Void arg) {
					n.addMarkerAnnotation(Entity.class);
					n.addAndGetAnnotation(Table.class).asNormalAnnotationExpr().addPair("name",
							"\"" + clazz.table + "\"");
					return super.visit(n, arg);

				};

				@Override
				public Visitable visit(final FieldDeclaration n, Void arg) {
					Annotate v = (Annotate) clazz.variables.get(n.getVariable(0).getName().getIdentifier());
					if (v != null) {
						v.annotateField(cu, n);
					}
					return super.visit(n, arg);
				}

			}, null);

			// This saves all the files we just read to an output directory.
			sourceRoot.saveAll(
					// The path of the Maven module/project which contains the LogicPositivizer
					// class.
					CodeGenerationUtils.mavenModuleRoot(LogicPositivizer.class)
							// appended with a path to "output"
							.resolve(Paths.get("output")));

		}
	}
}

class ParseFactory {
	public static ParseObject Create(Element e) {
		String tag = e.getTagName();
		switch (tag) {
		case "set":
			return new Set(e);
		case "key":
			return new Key(e);
		case "one-to-many":
			return new One(e);
		case "many-to-one":
			return new Many(e);
		case "property":
			return new Property(e);
		case "column":
			return new Column(e);
		case "id":
			return new Id(e);
		case "class":
			return new Clazz(e);
		case "hibernate-mapping":
			return new Mapping(e);
		case "generator":
			return new Generator(e);
		default:
			return null;
		}
	}

}

interface Annotate {
	public void annotateField(final CompilationUnit cu, final FieldDeclaration n);
}

abstract class ParseObject {
	String name;

	public static void doColumns(List<Column> columns, FieldDeclaration n, String name) {
		for (Column c : columns) {
			Log.info("doColumn: name is " + name + "   c is " + c);
			NormalAnnotationExpr x = n.addAndGetAnnotation(javax.persistence.Column.class).asNormalAnnotationExpr()
					.addPair("name", "\"" + name + "\"");
			if (c.getNotNull() != null) {
				x.addPair("nullable", "" + !c.getNotNull());
			}
			if (c.getLength() != null) {
				Log.info("Length = '" + c.getLength() + "'");
				x.addPair("length", "" + c.getLength());
			}
		}
	}

	public ParseObject(Element e) {
		name = e.getAttribute("name");
	}
}

class Mapping extends ParseObject {
	String packageName;
	List<Clazz> clazz = new ArrayList<>();

	public Mapping(Element e) {
		super(e);
		packageName = e.getAttribute("package");
		NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				clazz.add((Clazz) ParseFactory.Create((Element) n));
			}
		}
	}

	@Override
	public String toString() {
		return "Mapping [packageName=" + packageName + ", clazz=" + clazz + ", name=" + name + "]";
	}

}

class Clazz extends ParseObject {
	Map<String, ParseObject> variables = new HashMap<>();
	String table;
	String lazy;

	public Clazz(Element e) {
		super(e);
		table = e.getAttribute("table");
		lazy = e.getAttribute("lazy");

		NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				ParseObject o = ParseFactory.Create((Element) n);
				variables.put(o.name, o);
			}
		}
	}

	@Override
	public String toString() {
		return "Clazz [variables=" + variables + ", table=" + table + ", lazy=" + lazy + ", name=" + name + "]";
	}

}

class Id extends ParseObject implements Annotate {
	String type;
	Generator generator;
	List<Column> columns = new ArrayList<>();

	public Id(Element e) {
		super(e);
		type = e.getAttribute("type");
		NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				ParseObject o = ParseFactory.Create((Element) n);
				switch (((Element) n).getTagName()) {
				case "generator":
					generator = (Generator) o;
					break;
				case "column":
					columns.add((Column) o);
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Id [type=" + type + ", generator=" + generator + ", columns=" + columns + ", name=" + name + "]";
	}

	@Override
	public void annotateField(CompilationUnit cu, FieldDeclaration n) {
		doColumns(columns, n, this.name);
		n.addMarkerAnnotation(javax.persistence.Id.class);
		if (generator != null) {
			switch (generator.className) {
			case "native":
				n.addAndGetAnnotation(GeneratedValue.class).asNormalAnnotationExpr().addPair("strategy",
						"GenerationType.AUTO");
				cu.addImport(javax.persistence.GenerationType.class);
				break;
			case "assigned":
				break;
			default:
				n.addAndGetAnnotation(GeneratedValue.class).asNormalAnnotationExpr().addPair("strategy",
						"GenerationType.IDENTITY");
				cu.addImport(javax.persistence.GenerationType.class);
				break;
			}
		}
	}
}

class Generator extends ParseObject {
	String className;

	public Generator(Element e) {
		super(e);
		className = e.getAttribute("class");
	}

	@Override
	public String toString() {
		return "Generator [className=" + className + ", name=" + name + "]";
	}

}

class Property extends ParseObject implements Annotate {
	String type;
	Column column;

	public Property(Element e) {
		super(e);
		setType(e.getAttribute("type"));
		Element sub = (Element) e.getElementsByTagName("column").item(0);
		if (sub == null) {
			column = new Column(e);
		} else {
			column = new Column(sub);
		}
	}

	public void setType(String type) {
		if (type != null && !"".equals(type))
			this.type = type;
	}

	@Override
	public String toString() {
		return "Property [type=" + type + ", column=" + column + ", name=" + name + "]";
	}

	@Override
	public void annotateField(CompilationUnit cu, FieldDeclaration n) {
		List<Column> columns = new ArrayList<>();
		columns.add(column);
		doColumns(columns, n, this.name);
	}

}

class Set extends ParseObject implements Annotate {
	String inverse;
	Key key;
	One one;

	@Override
	public void annotateField(CompilationUnit cu, FieldDeclaration n) {
		Column c = key.getColumns().get(0);
		n.addAndGetAnnotation(OneToMany.class).asNormalAnnotationExpr().addPair("mappedBy", "\"" + key.getColumns().get(0).getName() + "\"");
		Log.info(n.getVariable(0).getTypeAsString());
		n.getVariable(0).setType("Set<" + one.className + ">");
	}

	public String getInverse() {
		return inverse;
	}

	public void setInverse(String inverse) {
		this.inverse = inverse;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public One getOne() {
		return one;
	}

	public void setOne(One one) {
		this.one = one;
	}

	public Set(Element e) {
		super(e);
		setInverse(e.getAttribute("inverse"));
		NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				ParseObject o = ParseFactory.Create((Element) n);
				switch (((Element) n).getTagName()) {
				case "key":
					key = (Key) o;
					break;
				case "one-to-many":
					one = (One) o;
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Set [inverse=" + inverse + ", key=" + key + ", one=" + one + ", name=" + name + "]";
	}

}

class Key extends ParseObject {
	List<Column> columns = new ArrayList<>();

	public Key(Element e) {
		super(e);
		Element sub = (Element) e.getElementsByTagName("column").item(0);
		columns.add(new Column(sub));
	}

	@Override
	public String toString() {
		return "Key [columns=" + columns + "]";
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

}

class One extends ParseObject {
	String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public One(Element e) {
		super(e);
		setClassName(e.getAttribute("class"));
	}

	@Override
	public String toString() {
		return "One [className=" + className + ", name=" + name + "]";
	}

}

class Many extends ParseObject implements Annotate {
	String lazy;
	String fetch;
	Column column;

	@Override
	public String toString() {
		return "Many [lazy=" + lazy + ", fetch=" + fetch + ", column=" + column + ", name=" + name + "]";
	}

	public Many(Element e) {
		super(e);
		setFetch(e.getAttribute("fetch"));
		setLazy(e.getAttribute("lazy"));
		NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				switch (((Element) n).getTagName()) {
				case "column":
					column = (Column) ParseFactory.Create((Element) n);
					break;
				default:
					break;
				}
			}
		}

	}

	public Boolean getLazy() {
		if (lazy == null || "".equals(lazy))
			return null;
		return Boolean.parseBoolean(lazy);
	}

	public void setLazy(String lazy) {
		this.lazy = lazy;
	}

	public String getFetch() {
		return fetch;
	}

	public void setFetch(String fetch) {
		this.fetch = fetch;
	}

	@Override
	public void annotateField(CompilationUnit cu, FieldDeclaration n) {
		n.addMarkerAnnotation(ManyToOne.class);
		NormalAnnotationExpr x = n.addAndGetAnnotation(JoinColumn.class).asNormalAnnotationExpr().addPair("name",
				"\"" + column.getName() + "\"");
		Boolean notNull = column.getNotNull();
		if (notNull != null) {
			if (notNull) {
				x.addPair("nullable", notNull ? "false" : "true");
			}
		}

	}

}

class Column extends ParseObject {
	private String type;
	private String length;
	private String sqlType;
	private String generator;
	private String unsavedValue;
	private String notNull;
	private Many many;

	public Many getMany() {
		return many;
	}

	public void setMany(Many many) {
		this.many = many;
	}

	public String getUnsavedValue() {
		return unsavedValue;
	}

	public void setUnsavedValue(String unsavedValue) {
		if (unsavedValue != null && !"".equals(unsavedValue))
			this.unsavedValue = unsavedValue;
	}

	public Boolean getNotNull() {
		if (notNull == null || "".equals(notNull))
			return null;
		return Boolean.parseBoolean(notNull);
	}

	public void setNotNull(String notNull) {
		this.notNull = notNull;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		if (generator != null && !"".equals(generator))
			this.generator = generator;
	}

	public Column(Element e) {
		super(e);
		setType(e.getAttribute("type"));
		setLength(e.getAttribute("length"));
		setSqlType(e.getAttribute("sql-type"));
		setNotNull(e.getAttribute("not-null"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (type != null && !"".equals(type))
			this.type = type;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		if (length != null && !"".equals(length))
			this.length = length;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		if (sqlType != null && !"".equals(sqlType))
			this.sqlType = sqlType;
	}

	@Override
	public String toString() {
		return "Variable [name=" + name + ", type=" + type + ", length=" + length + ", sqlType=" + sqlType
				+ ", generator=" + generator + "]";
	}

}
