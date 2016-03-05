package spt.seo.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FrmMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCountWord;
	private JTextField txtCountChar;
	private JTextField txtSelectFolder;
	private JTextArea txtInput = new JTextArea();
	private JTextArea txtKeyWord;
	
	
	@SuppressWarnings("rawtypes")
	private static JComboBox cbxChangeChar = new JComboBox();
	@SuppressWarnings("rawtypes")
	private static JComboBox cbxByChar = new JComboBox();
	@SuppressWarnings("rawtypes")
	private static JComboBox cbxChangeCharF = new JComboBox();
	@SuppressWarnings("rawtypes")
	private static JComboBox cbxByCharF = new JComboBox();
	
	
	private static String[] listItem = { " ", "-","_", ",", ";", ":", "(", ")" };
	private JTextField txtSuccess;
	private JTextField txtFail;
	private JTextField txtSum;
	private JTextField txtURL;
	
//	private File getLatestFilefromDir(String dirPath){
//	    File dir = new File(dirPath);
//	    File[] files = dir.listFiles();
//	    if (files == null || files.length == 0) {
//	        return null;
//	    }
//
//	    File lastModifiedFile = files[0];
//	    for (int i = 1; i < files.length; i++) {
//	       if (lastModifiedFile.lastModified() < files[i].lastModified()) {
//	           lastModifiedFile = files[i];
//	       }
//	    }
//	    return lastModifiedFile;
//	}
	
	private String getHTMLCodeByURL(String url) throws IOException {
		String source = "";
		try {
			
			URL u = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection)u.openConnection();
			InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			char[] buffer = new char[8192];
			int read = 0;
			StringBuilder builder = new StringBuilder();
						
			while ((read = reader.read(buffer)) != -1) {
				builder.append(buffer, 0, read);
			}
			
			source = builder.toString();
		}
		catch (MalformedURLException ex) {
		
			ex.printStackTrace();
		}
		return source;
	}
	
	private String findKeyWord(String Src) {
		
		int pos = Src.indexOf("\"keywords\":\"", 0);
		System.out.println("pos: " + pos);
		System.out.println("pos: " + pos);
		char ch = Src.charAt(pos + 11);
		String temp = "";
		while (ch != '"') {
			temp = temp + 	ch;
			
		}
		System.out.println("temp: " + temp);
		return temp;
	}
	
	private class SelectFolderAction implements ActionListener {
		JFileChooser finalJF = new JFileChooser("C:\\");
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser = finalJF;
			int value = fileChooser.showOpenDialog(null);
			if (value == JFileChooser.APPROVE_OPTION) {
				File f = fileChooser.getCurrentDirectory();
				txtSelectFolder.setText(f.getAbsolutePath());
				finalJF = new JFileChooser(f.getAbsolutePath());
			}
		}
				
	}
	
	private class ChangeFileName implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			File root = new File(txtSelectFolder.getText());
			File[] listFile = root.listFiles();
			
			int Success = 0;
			int Fail = 0;
			for (int i = 0; i < listFile.length; i++) {
				if (listFile[i].isFile()) {
//					String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
//		            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
//		            return pattern.matcher(temp).replaceAll("");
//					return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");

					String S = Normalizer.normalize(listFile[i].getName().trim(), Normalizer.Form.NFD);;
					Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
					S = pattern.matcher(S).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
					String R = "";
					
					String changeChar = cbxChangeCharF.getSelectedItem().toString();
					String byChar = cbxByCharF.getSelectedItem().toString();										
					
					String R1 = S.replaceAll(changeChar, byChar);
					R = R1.replaceAll("[()]", "");
					
					File oldFile = listFile[i];
					File newFile = new File(oldFile.getAbsolutePath().replace(oldFile.getName(), R));
					if (oldFile.renameTo(newFile)) {
						Success++;
					}
					else {
						Fail++;
					}					
				}
			}
			txtSuccess.setText(Integer.toString(Success));
			txtFail.setText(Integer.toString(Fail));
			txtSum.setText(Integer.toString(Fail + Success));
			JOptionPane.showMessageDialog(null, "Đã đổi xong");
		}
		
	}

	private class ConvertTextAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String S = txtInput.getText();
			int limit = S.length();
			String s = "";
			
			try {
				File f = new File("E:/config.xml");
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				Document document = builder.parse(f);
				
				Element root = document.getDocumentElement();
				NodeList list = root.getElementsByTagName("limitchar");
				s = list.item(0).getTextContent();
				
				limit = Integer.parseInt(s);
				
			} catch (ParserConfigurationException e1) {
				
				e1.printStackTrace();
			} catch (SAXException e1) {
				
				e1.printStackTrace();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
			
			S = S.substring(0, limit);
			txtInput.setText(S);
			
			String changeChar = cbxChangeChar.getSelectedItem().toString();
			String byChar = cbxByChar.getSelectedItem().toString();										
			
			String R = S.replaceAll(changeChar, byChar);
			txtInput.setText(R);
		}
		
	}
	
	private class countWordAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String S = txtInput.getText().trim();
			String[] arrS = S.split("[\\ \\,\\.\\\\\\/\\|\\:\\;\\'\\?\\(\\)\\\r\n\\_\\-]+");
			txtCountWord.setText(Integer.toString(arrS.length));
		}
		
	}
	
	private class countCharAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String S = new String(); 
			S = txtInput.getText();			
			txtCountChar.setText(Integer.toString(S.length()));
		}
		
	}

	private class getKeyWordAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			String url = txtURL.getText().trim();
			System.out.println(url);
			String S = "";
			String Re = "";
			try {
				S = getHTMLCodeByURL(url);
				//System.out.println("S: " + S);
				Re = findKeyWord(S);
				System.out.println("Key: " + Re);
				txtKeyWord.setText(Re);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmMain frame = new FrmMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FrmMain() {
		setTitle("SEO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 592, 355);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnOption = new JMenu("Lựa chọn");
		menuBar.add(mnOption);
		
		JMenuItem mntmCharLimit = new JMenuItem("Giới hạn kí tự");
		mntmCharLimit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String inp = "";
				inp = JOptionPane.showInputDialog(null, "Nhập số ký tự tối đa", inp);
				
				System.out.println(inp);
				
				try {
					File f = new File("E:/config.xml");
					//f.canWrite();
					if (f.createNewFile()) {
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						DocumentBuilder build = factory.newDocumentBuilder();
						Document doc = build.newDocument();
						
						
						Element root = doc.createElement("root");
						doc.appendChild(root);
						
						Node limitchar = doc.createElement("limitchar");
						limitchar.setTextContent(inp);
						
						root.appendChild(limitchar);
						
						Transformer tr = TransformerFactory.newInstance().newTransformer();
						DOMSource src = new DOMSource(doc);
						StreamResult re = new StreamResult(f);
						
						tr.transform(src, re);
					}
					
					

				} catch (ParserConfigurationException e) {
					
					e.printStackTrace();
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerFactoryConfigurationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
;
			}
		});
		mnOption.add(mntmCharLimit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane containTab = new JTabbedPane(JTabbedPane.TOP);
		containTab.setBounds(0, 0, 576, 295);
		contentPane.add(containTab);
		
		JPanel panelRenameFile = new JPanel();
		containTab.addTab("Đổi tên file", null, panelRenameFile, null);
		panelRenameFile.setLayout(null);
		
		JLabel lblSelectFolder = new JLabel("Chọn thư mục");
		lblSelectFolder.setBounds(10, 10, 80, 20);
		panelRenameFile.add(lblSelectFolder);
		
		txtSelectFolder = new JTextField();
		txtSelectFolder.setBounds(116, 10, 305, 20);
		panelRenameFile.add(txtSelectFolder);
		txtSelectFolder.setColumns(10);
		
		JButton btnSelectFolder = new JButton("...");
		btnSelectFolder.addActionListener(new SelectFolderAction());
		btnSelectFolder.setBounds(447, 9, 89, 23);
		panelRenameFile.add(btnSelectFolder);
		
		JLabel lbli = new JLabel("Đổi");
		lbli.setBounds(10, 63, 80, 20);
		panelRenameFile.add(lbli);
		
		
		cbxChangeCharF.setModel(new DefaultComboBoxModel(listItem));
		cbxChangeCharF.setBounds(116, 63, 89, 20);
		panelRenameFile.add(cbxChangeCharF);
		
		JLabel lblBng_1 = new JLabel("bằng");
		lblBng_1.setBounds(227, 63, 61, 20);
		panelRenameFile.add(lblBng_1);
			
		cbxByCharF.setModel(new DefaultComboBoxModel(listItem));
		cbxByCharF.setBounds(324, 63, 97, 20);
		panelRenameFile.add(cbxByCharF);
		
		JButton btniTn = new JButton("Đổi tên");
		btniTn.setFont(new Font("Times New Roman", Font.BOLD, 14));
		btniTn.addActionListener(new ChangeFileName());
		btniTn.setBounds(173, 107, 189, 29);
		panelRenameFile.add(btniTn);
		
		JLabel lblSuccess = new JLabel("Thành công");
		lblSuccess.setBounds(10, 160, 80, 20);
		panelRenameFile.add(lblSuccess);
		
		txtSuccess = new JTextField();
		txtSuccess.setEditable(false);
		txtSuccess.setBounds(116, 160, 86, 20);
		panelRenameFile.add(txtSuccess);
		txtSuccess.setColumns(10);
		
		JLabel lblFail = new JLabel("Thất bại");
		lblFail.setBounds(245, 162, 80, 17);
		panelRenameFile.add(lblFail);
		
		txtFail = new JTextField();
		txtFail.setEditable(false);
		txtFail.setBounds(353, 160, 86, 20);
		panelRenameFile.add(txtFail);
		txtFail.setColumns(10);
		
		JLabel lblSum = new JLabel("Tổng");
		lblSum.setBounds(52, 218, 80, 20);
		panelRenameFile.add(lblSum);
		
		txtSum = new JTextField();
		txtSum.setEditable(false);
		txtSum.setBounds(218, 218, 86, 20);
		panelRenameFile.add(txtSum);
		txtSum.setColumns(10);
		
		JPanel panelConvertText = new JPanel();
		containTab.addTab("Đổi ký tự", null, panelConvertText, null);
		panelConvertText.setLayout(null);
		
		JLabel lblInput = new JLabel("Nhập văn bản");
		lblInput.setBounds(10, 10, 80, 20);
		panelConvertText.add(lblInput);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(117, 8, 418, 135);
		panelConvertText.add(scrollPane);
					
		scrollPane.setViewportView(txtInput);
		
		txtCountWord = new JTextField();
		txtCountWord.setEditable(false);
		txtCountWord.setBounds(117, 234, 118, 20);
		panelConvertText.add(txtCountWord);
		txtCountWord.setColumns(10);
		
		JButton btnCountWord = new JButton("Số từ");
		btnCountWord.addActionListener(new countWordAction());
		btnCountWord.setBounds(10, 233, 80, 23);
		panelConvertText.add(btnCountWord);
		
		JButton btnConvert = new JButton("Đổi kí tự");
		btnConvert.addActionListener(new ConvertTextAction());
		btnConvert.setBounds(229, 200, 89, 23);
		panelConvertText.add(btnConvert);
		
		JLabel lblThay = new JLabel("Thay");
		lblThay.setBounds(10, 160, 80, 20);
		panelConvertText.add(lblThay);
				
		cbxChangeChar.setModel(new DefaultComboBoxModel(listItem));
		cbxChangeChar.setBounds(117, 160, 70, 20);
		panelConvertText.add(cbxChangeChar);
		
		JLabel lblBng = new JLabel("bằng");
		lblBng.setBounds(243, 160, 80, 20);
		panelConvertText.add(lblBng);
				
		cbxByChar.setModel(new DefaultComboBoxModel(listItem));
		cbxByChar.setBounds(368, 160, 70, 20);
		panelConvertText.add(cbxByChar);
		
		JButton btnCountChar = new JButton("Số ký tự");
		btnCountChar.addActionListener(new countCharAction());
		btnCountChar.setBounds(269, 233, 89, 23);
		panelConvertText.add(btnCountChar);
		
		txtCountChar = new JTextField();
		txtCountChar.setEditable(false);
		txtCountChar.setBounds(378, 234, 137, 20);
		panelConvertText.add(txtCountChar);
		txtCountChar.setColumns(10);
		
		JPanel panelGetKeyWord = new JPanel();
		containTab.addTab("Tìm Keyword", null, panelGetKeyWord, null);
		panelGetKeyWord.setLayout(null);
		
		JLabel lblURL = new JLabel("Nhập URL");
		lblURL.setBounds(10, 10, 80, 20);
		panelGetKeyWord.add(lblURL);
		
		txtURL = new JTextField();
		txtURL.setBounds(110, 10, 419, 20);
		panelGetKeyWord.add(txtURL);
		txtURL.setColumns(10);
		
		JLabel lblKeyword = new JLabel("KeyWord");
		lblKeyword.setBounds(10, 100, 80, 20);
		panelGetKeyWord.add(lblKeyword);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(110, 98, 419, 143);
		panelGetKeyWord.add(scrollPane_1);
		
		txtKeyWord = new JTextArea();
		scrollPane_1.setViewportView(txtKeyWord);
		
		JButton btnFind = new JButton("Tìm");
		btnFind.addActionListener(new getKeyWordAction());
		btnFind.setBounds(264, 52, 89, 23);
		panelGetKeyWord.add(btnFind);
	}
}
