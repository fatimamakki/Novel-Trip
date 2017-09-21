import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

class Interface extends JFrame
{
	JButton start,search,explore,end,edit,check;
	JList myList, charList;
	JTextField t1,t2;
	String title;
	int dx,dy;
	JLabel l1,labelImage,Results, lTitle, imageUrl;
	ImageIcon image;
	ParseNovel parseObject;
	JScrollPane scrollPane, scrollPane2;
	List<LocationInfo> locations;
	MouseListener mouseListener ;
	Color blue = new Color(60,56,150);
	Color blue2 = new Color(66,92,158);
	Color purple = new Color(153,0,153);
	String filename = "";
	Font f;
	public Interface() throws IOException, URISyntaxException 
	{
		setTitle("Novel Trip");
		setSize(400, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setVisible(true);
		// Another way
		setLayout(new BorderLayout());
		final Image background = ImageUtils.scaleImage(982, 557, ImageIO.read(new File("Images/Novel Trip background.jpg")));
		final Image background2 = ImageUtils.scaleImage(982, 557, ImageIO.read(new File("Images/Explore background.jpg")));
		final Image background3 = ImageUtils.scaleImage(982, 557, ImageIO.read(new File("Images/Listing background.jpg")));
		final Image background4 = ImageUtils.scaleImage(982, 557, ImageIO.read(new File("Images/Thanks background.jpg")));
		final Image startIcon = ImageUtils.scaleImage(80, 50, ImageIO.read(new File("Images/footprints.jpg")));  
		final Image searchIcon = ImageUtils.scaleImage(50,50, ImageIO.read(new File("Images/search3.jpg")));   
		final Image exploreIcon = ImageUtils.scaleImage(100, 100, ImageIO.read(new File("Images/explore.jpg")));    
		final Image endIcon = ImageUtils.scaleImage(100, 100, ImageIO.read(new File("Images/end.jpg")));      
		final Image checkIcon = ImageUtils.scaleImage(100, 100, ImageIO.read(new File("Images/check.jpg")));      
		final Image loadIcon = ImageUtils.scaleImage(400, 400, ImageIO.read(new File("Images/newLoad.jpg")));      
		setContentPane(new JLabel(new ImageIcon(background)));
		setLayout(new FlowLayout());

		JPanel pnlButton = new JPanel();
		//Buttons
		start= new JButton(new ImageIcon(startIcon));
		search= new JButton(new ImageIcon(searchIcon));
		explore= new JButton(new ImageIcon(exploreIcon));
		end= new JButton(new ImageIcon(endIcon));
		check= new JButton(new ImageIcon(checkIcon));
		edit= new JButton(new ImageIcon("Images/editImage2.png"));
		Results = new JLabel("Results for:");


		setLayout(null);
		//Adding to JFrame
		pnlButton.setLayout(null);
		start.setBounds(600, 300, 80, 50) ;
		start.setPressedIcon(new ImageIcon(startIcon));
		pnlButton.add(start);
		add(pnlButton);
		pnlButton.setLocation(50,100);

		start.setBorder(null);
		pnlButton.setBorder(null);
		start.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				final JFrame frame = new JFrame();
				try {
					f = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")).deriveFont(23f);
					GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
					//register the font
					ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")));

				}
				catch (FontFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				frame.setTitle("Novel Trip");
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.setLayout(new BorderLayout());
				frame.setContentPane(new JLabel(new ImageIcon(background2)));
				frame.setLayout(null);
				l1=new JLabel("Novel Title:");
				l1.setFont(f.deriveFont(28f));
				l1.setForeground(blue);
				l1.setBounds(70,150,250,40);
				t1 =new JTextField();
				t1.setBounds(200,150,200,50);
				t1.setForeground(purple);
				t1.setFont(f.deriveFont(16));
				t1.setHorizontalAlignment(JTextField.CENTER);
				t1.setBorder(BorderFactory.createMatteBorder(2,2,2,2,blue2));
				final JLabel l2=new JLabel("and");
				l2.setFont(f.deriveFont(28f));
				l2.setForeground(blue);
				l2.setBounds(450,150,60,40);
				//*****************************************
				JLabel l3=new JLabel("Upload Novel:");
				l3.setFont(f.deriveFont(28f));
				l3.setForeground(blue);
				l3.setBounds(540,150,250,40);
				//*******************************************

				t2 =new JTextField(" Browse to your file");
				t2.setFont(f.deriveFont(18));
				t2.setForeground(purple);
				t2.setEditable(false);
				//t2.setEnabled(false);
				t2.setBounds(700,150,200,50);
				t2.setBorder(BorderFactory.createMatteBorder(2,2,2,2,blue2));
				search.setBounds(930, 150, 47, 47);
				search.setPressedIcon(new ImageIcon(searchIcon));
				search.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						JFileChooser fileChooser = new JFileChooser();
						int result = fileChooser.showOpenDialog(frame);
						if (result == JFileChooser.APPROVE_OPTION)
							filename = fileChooser.getSelectedFile().toString();
						if(!filename.equals("")){
							String[] path = filename.split("/");
							t2.setText(" " +path[path.length-1] + " ");
							//System.out.println("type: "+path[path.length-1].split("\\.")[1]);
						}
						//parse file name
					}

				});
				explore.setBounds(670, 300, 100, 100) ;
				explore.setOpaque(false);
				explore.setPressedIcon(new ImageIcon(exploreIcon));
				explore.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						if(!t2.getText().equals(" Browse to your file") && !t1.getText().equals("")){
							title = t1.getText();
							final JFrame frame1 = new JFrame();
							try {
								f = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")).deriveFont(23f);
								GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
								//register the font
								ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")));

							}
							catch (FontFormatException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							frame1.setTitle("Novel Trip");
							frame1.setLocationRelativeTo(null);
							//frame1.setDefaultCloseOperation(EXIT_ON_CLOSE);
							frame1.setVisible(true);
							frame1.setLayout(new BorderLayout());
							frame1.setContentPane(new JLabel(new ImageIcon(background3)));
							frame1.setLayout(null);
							frame1.setSize(982, 557);  
							frame1.setLocation(dx, dy);
							Results.setForeground(blue);
							Results.setFont(f.deriveFont(30f));
							Results.setBounds(50,200,600,40);
							lTitle = new JLabel(title);
							lTitle.setFont(f.deriveFont(30f));
							lTitle.setForeground(purple);
							lTitle.setBounds(50,260,600,40);
							end.setBounds(850, 400, 100, 100) ;
							//end.setOpaque(false);
							end.setPressedIcon(new ImageIcon(endIcon));
			
							check.setBounds(850, 200, 100, 100) ;
							//check.setOpaque(false);
							check.setPressedIcon(new ImageIcon(checkIcon));
							//frame1.add(scrollPane);
							frame1.add(check);
							frame1.add(end);
							frame1.add(lTitle);
							frame1.add(Results);
							//************************************************
							
							//****************loading frame*********************
							final JFrame frameLoad = new JFrame("Parsing Novel...");
						    frameLoad.setVisible(true);
							frameLoad.setContentPane(new JLabel(new ImageIcon(loadIcon)));
							frameLoad.setLayout(null);
							frameLoad.setSize(400, 400);  
							frameLoad.setLocation(440, 220);
						    final JProgressBar progressBar = new JProgressBar();
						    progressBar.setIndeterminate(true);
						    progressBar.setBounds(100, 320, 200, 50);
						    frameLoad.add(progressBar);
							frameLoad.setLocation(440, 220);
						    frameLoad.setSize(400, 400);  

						    Runnable runnable = new Runnable() {
						        public void run() {
						            // do loading stuff in here
						            // for now, simulate loading task with Thread.sleep(...)
	
										try {

											parseObject = new ParseNovel(filename,title);
											//parse it
											parseObject.parsing();
											//get the list
											locations = parseObject.getList();
										}
										catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										catch (ClassCastException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										catch (ClassNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
						
										

						            // when loading is finished, make frame disappear
						            SwingUtilities.invokeLater(new Runnable() {
						                public void run() {

						                    try {
						                    	System.out.println("************** " + locations.size() );

												setContent(frame1);
						                    	myList.setCellRenderer(new MyCellRenderer());
											frame1.add(scrollPane);
											}
											catch (ClassCastException
													| ClassNotFoundException
													| IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
						                    frameLoad.setVisible(false);
						                    frameLoad.dispose();
						                }
						            });

						        }
						    };
						    new Thread(runnable).start();
						    
						    //************loading frame ****************************************
							end.addActionListener(new ActionListener(){

								@Override
								public void actionPerformed(ActionEvent e) {
									final JFrame frame2 = new JFrame();
									try {
										f = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")).deriveFont(23f);
										GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
										//register the font
										ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")));

									}
									catch (FontFormatException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									frame2.setTitle("Novel Trip");
									frame2.setLocationRelativeTo(null);
									//frame2.setDefaultCloseOperation(EXIT_ON_CLOSE);
									frame2.setVisible(true);
									frame2.setLayout(new BorderLayout());
									frame2.setContentPane(new JLabel(new ImageIcon(background4)));
									//frame1.setLayout(null);
									frame2.setSize(982, 557);  
									frame2.setLocation(dx, dy);
									ActionListener actListner = new ActionListener() {

										@Override
										public void actionPerformed(
												ActionEvent e) {
											frame.dispose();
											frame2.dispose();frame1.dispose();

										}

									};
									Timer timer = new Timer(1500, actListner);
									timer.start();

								}


							});

						}
						else{ //nothing is choosed
							if(t2.getText().equals(" Browse to your file"))
								JOptionPane.showMessageDialog(frame,
										"You should upload a novel document!",
										"Input error",
										JOptionPane.ERROR_MESSAGE);
							else if (t1.getText().equals(""))
								JOptionPane.showMessageDialog(frame,
										"You should enter a novel title!",
										"Input error",
										JOptionPane.ERROR_MESSAGE);

						}
					}

					private void setContent(JFrame frame3) throws IOException, ClassCastException, ClassNotFoundException {
						// TODO Auto-generated method stub
						//create parse object
//						parseObject = new ParseNovel(path);
//						//parse it
//						parseObject.parsing();
//						//get the list
						locations = parseObject.getList();
						System.out.println("Size = " + locations.size());
						myList = new JList(toStrings());
						//display the list
						ListModel model = myList.getModel();
						for(int i = 0; i < model.getSize(); i++) {
							System.out.println(model.getElementAt(i));
						}
						myList.setFixedCellHeight(100);
						myList.setFont(f);
						myList.setCellRenderer(new MyCellRenderer());
						mouseListener = new MouseAdapter() {
							public void mouseClicked(MouseEvent e) {
								if (e.getClickCount() == 2) {
									int index = myList.locationToIndex(e.getPoint());
									//create Details Page
									final JFrame Details = new JFrame();
									try {
										f = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")).deriveFont(23f);
										GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
										//register the font
										ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")));

									}
									catch (FontFormatException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									Details.setTitle("Novel Trip");
									Details.setLocationRelativeTo(null);
									Details.setVisible(true);
									Details.setLayout(new BorderLayout());
									Details.setContentPane(new JLabel(new ImageIcon(background3)));
									Details.setLayout(null);
									//Location:
									JLabel loc =new JLabel("Location: ");
									loc.setFont(f.deriveFont(28f));
									loc.setForeground(blue);
									loc.setBounds(20,100,150,40);
									//Location Name
									final String LocationName = capitalize(locations.get(index).name);
									JLabel name =new JLabel(LocationName);
									name.setFont(f.deriveFont(28f));
									name.setForeground(purple);
									name.setBounds(160,100,200,40);
									//Associated Characters:
									JLabel chars =new JLabel("Associated Characters:");
									chars.setFont(f.deriveFont(28f));
									chars.setForeground(blue);
									chars.setBounds(20,180,400,40);
									//List of Characters
									charList = new JList(locations.get(index).characters.toArray());
									//display the list
									ListModel model2 = charList.getModel();
									for(int i = 0; i < model2.getSize(); i++) {
										System.out.println(model2.getElementAt(i));
									}
									charList.setCellRenderer(new MyCellRenderer2());
									charList.setFixedCellHeight(70);
									charList.setFont(f.deriveFont(26f));
									scrollPane2 = new JScrollPane(charList);
									scrollPane2.setBorder(BorderFactory.createMatteBorder(2,2,2,2,blue2));
									scrollPane2.setBounds(20, 220, 200, 300);

									//Image View
									//search for image with location name
//									try {
										//BufferedImage img = ImageIO.read(new File("Images/"+LocationName + ".png"));
										//image = new ImageIcon(img);
//										image = new ImageIcon(locations.get(index).image);
//										Image scaleImage = image.getImage().getScaledInstance(400,400,Image.SCALE_DEFAULT);
//										image = new ImageIcon(scaleImage);
//								}
//									catch (IOException e2) {
//										// TODO Auto-generated catch block
//										e2.printStackTrace();
//										image = new ImageIcon("logo.png");
//									}
									//else show default


									//Choose Image
									edit.setBounds(850, 400, 100, 100) ;
									edit.setOpaque(false);
									//edit.setPressedIcon(new ImageIcon(editIcon));
									edit.addActionListener(new ActionListener(){
										String imageFile;
										@Override
										public void actionPerformed(ActionEvent e) {
											// TODO Auto-generated method stub
											JFileChooser fileChooser = new JFileChooser();
											int result = fileChooser.showOpenDialog(Details);
											if (result == JFileChooser.APPROVE_OPTION)
												imageFile = fileChooser.getSelectedFile().toString();
											try {
												BufferedImage img = ImageIO.read(new File(imageFile));
												//save image with location name
												ImageIO.write(img, "png",new File("Images/"+LocationName + ".png"));
												image = new ImageIcon(img);
												labelImage.setIcon(image);
											}
											catch (IOException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											//parse file name
										}

									});

//									imageUrl = new JLabel("image Url");
//									imageUrl.setBounds(400, 150, 300, 50);
//									imageUrl.setFont(f.deriveFont(28f));
//									imageUrl.setForeground(blue);
								
								    //******************************************************
									Details.add(loc);
									Details.add(name);
									Details.add(chars);
									Details.add(scrollPane2);
									Details.add(edit);
									//Details.add(imageUrl);
									Details.setSize(982, 557);  
									Details.setLocation(dx, dy);
									boolean succeed = (locations.get(index).image!=null);
//									try {
//										succeed = true;
//										locations.get(index).chooseImage();
//									}
//									catch (Exception e1) {
//										succeed = false;
//										System.out.println("Error in saving image");
//										e1.printStackTrace();
//									}
									System.out.println(locations.get(index).text);
									if(succeed){
									image = new ImageIcon(locations.get(index).image);
									Image scaleImage = image.getImage().getScaledInstance(400,400,Image.SCALE_DEFAULT);
									image = new ImageIcon(scaleImage);
									}
									else{
										BufferedImage img = null;
										try {
											img = ImageIO.read(new File("Images/"+LocationName + ".png"));
											image = new ImageIcon(img);
										}
										catch (IOException e1) {
											// TODO Auto-generated catch block
											image = new ImageIcon("Images/logo.png");
											e1.printStackTrace();
										}
	
									}
									labelImage = new JLabel(image);
									labelImage.setBounds(400, 130, 400, 400);
									Details.add(labelImage);
									//*************image URL********************
									JButton URLbutton = new JButton();
								    //URLbutton.setText("<HTML>Click the <FONT color=\"#000099\"><U>link</U></FONT>"
								       // + " to go to see the image.</HTML>");
									String url= locations.get(index).imageUrl;
								    URLbutton.setText("<HTML><a href=" + url +">Link to the image</a></HTML>");
								    URLbutton.setHorizontalAlignment(SwingConstants.LEFT);
								    URLbutton.setBorderPainted(false);
								    URLbutton.setOpaque(false);
								    URLbutton.setBackground(Color.WHITE);

									
									final URI uri;
									try {
										uri = new URI(url);
									    URLbutton.setToolTipText(uri.toString());
										class OpenUrlAction implements ActionListener {
										      @Override public void actionPerformed(ActionEvent e) {
										        open(uri);
										      }
										    }
									    URLbutton.addActionListener(new OpenUrlAction());
									}
									catch (URISyntaxException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
								    URLbutton.setBounds(400, 90, 300, 40);
								    URLbutton.setFont(f.deriveFont(18f));
								    Details.add(URLbutton);
									
								}
							}
						};
						myList.addMouseListener(mouseListener);
						scrollPane = new JScrollPane(myList);
						scrollPane.setBorder(BorderFactory.createMatteBorder(2,2,2,2,blue2));
						scrollPane.setBounds(350, 110, 300, 400);
						frame3.add(scrollPane);
					}
				});
				frame.add(l1);
				frame.add(t1);
				//frame.add(l2);
				frame.add(l3);
				frame.add(t2);
				frame.add(search);
				frame.add(explore);
				frame.setSize(982, 557);
				Dimension windowSize = frame.getSize();
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				Point centerPoint = ge.getCenterPoint();

				dx = centerPoint.x - windowSize.width / 2;
				dy = centerPoint.y - windowSize.height / 2;    
				frame.setLocation(dx, dy);
			}

		});
		//add(l1);
		add(start);
		// Just for refresh :) Not optional!
		//setSize(982, 557);
		setSize(982, 557);
		Dimension windowSize = getSize();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point centerPoint = ge.getCenterPoint();

		dx = centerPoint.x - windowSize.width / 2;
		dy = centerPoint.y - windowSize.height / 2;    
		setLocation(dx, dy);
	}
	public String[] toStrings(){
		String[] names = new String[locations.size()];
		int i =0;
		for(LocationInfo loc : locations)
			names[i++] = loc.name;
		return names;
	}
	public static void main(String args[]) throws IOException, URISyntaxException
	{
		new Interface();
	}
	  private static void open(URI uri) {
	        if (Desktop.isDesktopSupported()) {
	          try {
	            Desktop.getDesktop().browse(uri);
	          } catch (IOException e) { /* TODO: error handling */ }
	        } else { System.out.println("passed here");}
	      }
	public String capitalize(String s){
		StringBuffer res = new StringBuffer();

		String[] strArr = s.split(" ");
		for (String str : strArr) {
			char[] stringArray = str.trim().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			str = new String(stringArray);

			res.append(str).append(" ");
		}

		return res.toString().trim();
	}
}

class MyCellRenderer  extends JLabel implements ListCellRenderer{

	final ImageIcon firstIcon, secondIcon, thirdIcon, allIcon;
	// This is the only method defined by ListCellRenderer.
	// We just reconfigure the JLabel each time we're called.
	public String capitalize(String s){
		StringBuffer res = new StringBuffer();

		String[] strArr = s.split(" ");
		for (String str : strArr) {
			char[] stringArray = str.trim().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			str = new String(stringArray);

			res.append(str).append(" ");
		}

		return res.toString().trim();
	}

	public MyCellRenderer() throws IOException{
		firstIcon = new ImageIcon("Images/list2.png");
		secondIcon = new ImageIcon("Images/list3.png");
		thirdIcon = new ImageIcon("Images/list1.png");
		allIcon = new ImageIcon("Images/list4.png");
	}

	public Component getListCellRendererComponent(
			JList list,              // the list
			Object value,            // value to display
			int index,               // cell index
			boolean isSelected,      // is the cell selected
			boolean cellHasFocus)    // does the cell have focus
	{
		String s = value.toString();
		setText(capitalize(s));
		switch(index){
		case 0:setIcon(firstIcon); break;
		case 1:setIcon(secondIcon); break;
		case 2:setIcon(thirdIcon); break;
		default:setIcon(allIcon); break;
		}
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true);
		return this;
	}
	
}


class MyCellRenderer2  extends JLabel implements ListCellRenderer{

	// This is the only method defined by ListCellRenderer.
	// We just reconfigure the JLabel each time we're called.
	public String capitalize(String s){
		StringBuffer res = new StringBuffer();
		String r;
		String[] strArr = s.split(" ");
		if(strArr.length == 0) {r = Character.toUpperCase(s.charAt(0)) + s.substring(1); return r;}
		for (String str : strArr) {
			char[] stringArray = str.trim().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			str = new String(stringArray);

			res.append(str).append(" ");
		}

		return res.toString().trim();
	}

	public Component getListCellRendererComponent(
			JList list,              // the list
			Object value,            // value to display
			int index,               // cell index
			boolean isSelected,      // is the cell selected
			boolean cellHasFocus)    // does the cell have focus
	{
		String s = "  " + Character.toUpperCase(value.toString().charAt(0)) + value.toString().substring(1);
		setText(s);
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true);
		return this;
	}
}




class ImageUtils {

	public static BufferedImage scaleImage(int width, int height, String filename) {
		BufferedImage bi;
		try {
			ImageIcon ii = new ImageIcon("Images/"+filename);
			bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = (Graphics2D) bi.createGraphics();
			g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
			g2d.drawImage(ii.getImage(), 0, 0, width, height, null);
		} catch (Exception e) {
			return null;
		}
		return bi;
	}

	static Image scaleImage(int width, int height, BufferedImage filename) {
		BufferedImage bi;
		try {
			bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = (Graphics2D) bi.createGraphics();
			g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
			g2d.drawImage(filename, 0, 0, width, height, null);
		} catch (Exception e) {
			return null;
		}
		return bi;
	}

}

class Rb  extends JFrame {
	Rb (){
		JRadioButton isLoc = new JRadioButton("Location");
		JRadioButton isChar = new JRadioButton("Character");
		JRadioButton other = new JRadioButton("Other");
		ButtonGroup bG = new ButtonGroup();
		bG.add(isLoc);
		bG.add(isChar);
		bG.add(other);
		this.setSize(100,200);
		this.setLayout( new FlowLayout());
		this.add(isLoc);
		this.add(isChar);
		this.add(other);
		isLoc.setSelected(true);
		this.setVisible(true);
	}
}

  