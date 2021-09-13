package lottoNumberMaker;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import loginproject.LoginSystemDBModel;
import loginproject.LoginSystemMain;
import loginproject.MemberInfo;
import loginproject.SendEmail;

public class LottoNumberMaker extends JFrame{
	//iv
	LoginSystemDBModel dbModel = new LoginSystemDBModel();
	MemberInfo memberInfo = dbModel.getMemberInfo("id", LoginSystemMain.loginUserId);
	private String loginUserId = memberInfo.getId();
	SendEmail sendEmail = new SendEmail();
	JButton makeNumberButton, spendMoneyButton, sendMailToUserButton, refreshButton;
	JLabel lottoNumberTitleLabel, spendMoneyTitleLabel;
	JLabel [] array ;
	DefaultTableModel model;
	JTable table;
	Vector<MemberInfo> list = new Vector<MemberInfo>();
	DecimalFormat df = new DecimalFormat("#,####");

	//�ζǹ�ȣ���� �÷���
	Set<Long> lottoNumberSet = new HashSet<Long>();
	List<Long> lottoNumber; 
	//�ζǹ�ȣ
	String lotto;
	//��÷����Һ�ݾ�
	String sumResult;


	boolean lottoNumberMakeUsed = false;
	boolean expectedMoneyUsed = false;
	long money = 1000;
	long sum = 0;

	//constructor
	public LottoNumberMaker(){
		super("������ �ζ� ��ȣ");
		this.setBounds(200,100, 1200, 400);
		this.setDesign();
		LoginSystemMain.programOn = true;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dbModel.cleanLoginUserTable(loginUserId);
				System.exit(0);
			}
		});
		this.setVisible(true);
	}

	//methods
	private void dataWriteOnTable() {
		// TODO Auto-generated method stub
		list = dbModel.getMemberInfoForTable();
		model.setRowCount(0);
		for(MemberInfo member :list)
		{
			Vector<String> data=new Vector<String>();
			data.add(member.getId());
			data.add(member.getName());
			data.add(member.getEmail());
			data.add(member.getSubscribe());
			//���̺�𵨿� �߰�
			model.addRow(data);		
		}
	}


	private void setDesign() {
		//label
		lottoNumberTitleLabel = new JLabel("��÷�����ȣ!!!");
		lottoNumberTitleLabel.setBounds(200, 50, 200, 30 );
		this.add(lottoNumberTitleLabel);

		lottoNumberTitleLabel = new JLabel("�ش��ȣ�� ��÷���� ���� �Һ�ݾ�");
		lottoNumberTitleLabel.setBounds(155, 200, 200, 30 );
		this.add(lottoNumberTitleLabel);

		JLabel l1 = new JLabel();
		l1.setBounds(90,90,50,50);
		l1.setBorder(new LineBorder(Color.red, 3));
		this.add(l1);
		JLabel l2 = new JLabel();
		l2.setBounds(144,90,50,50);
		l2.setBorder(new LineBorder(Color.red, 3));
		this.add(l2);
		JLabel l3 = new JLabel();
		l3.setBounds(198,90,50,50);
		l3.setBorder(new LineBorder(Color.red, 3));
		this.add(l3);
		JLabel l4 = new JLabel();
		l4.setBounds(252,90,50,50);
		l4.setBorder(new LineBorder(Color.red, 3));
		this.add(l4);
		JLabel l5 = new JLabel();
		l5.setBounds(306,90,50,50);
		l5.setBorder(new LineBorder(Color.red, 3));
		this.add(l5);
		JLabel l6 = new JLabel();
		l6.setBounds(360,90,50,50);
		l6.setBorder(new LineBorder(Color.red, 3));
		this.add(l6);
		JLabel moneyResultLable = new JLabel();
		moneyResultLable.setBounds(195,250,110, 30);
		moneyResultLable.setBorder(new LineBorder(Color.red, 3));
		this.add(moneyResultLable);
		array = new JLabel[] {l1, l2, l3, l4, l5, l6};



		//table
		String []title= {"ID","NAME","EMAIL","SUBSCRIBE"};
		model=new DefaultTableModel(title, 0);
		table=new JTable(model);
		JScrollPane js=new JScrollPane(table);
		js.setBounds(500, 80, 550, 250);
		this.add(js);
		dataWriteOnTable();

		//button
		sendMailToUserButton = new JButton("SEND EMAIL TO USER");
		sendMailToUserButton.setBounds(550,30,200,30);
		this.add(sendMailToUserButton);
		sendMailToUserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!expectedMoneyUsed) {
					JOptionPane.showMessageDialog(LottoNumberMaker.this, "�ζ� ��ȣ�� �����ϰ� �����ȣ�� �������ּ���");
					return;
				}
				String subject = "������ �ζ� ��÷��ȣ�Դϴ�.";
				String text = "������ ���� ��÷��ȣ " + lotto + " �ش翹�� ��÷��ȣ�� ��÷���� ����Һ�ݾ� " + sumResult + "��"
						+ "�Դϴ�. GOOD LUCK!!!";
				System.out.println(text);
				ArrayList<String> list = dbModel.getSubscriberEmail();
				//�̸��� ���״� �޼���
				for(int i=0; i<list.size(); i++){
					sendEmail.sendEmailToUser(list.get(i), subject, text);

				}
				JOptionPane.showMessageDialog(sendMailToUserButton, "�̸����� �߼ۿϷ� �߽��ϴ�.");
			}
		});

		refreshButton = new JButton("REFRESH");
		refreshButton.setBounds(800,30,200,30);
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dataWriteOnTable();
			}
		});
		this.add(refreshButton);

		this.setLayout(null);
		makeNumberButton = new JButton("��÷��ȣ ����");
		makeNumberButton.setBounds(150,160, 200, 30);
		this.add(makeNumberButton);
		makeNumberButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lottoNumberMakeUsed = true;

//				moneyResultLable.setText("");
				lottoNumberSet.removeAll(lottoNumberSet);
				for(int i=0; lottoNumberSet.size()<6; i++) {
					lottoNumberSet.add((long)(Math.random()*45+1));
				}
				lottoNumber = new LinkedList<Long>(lottoNumberSet);
				Collections.sort(lottoNumber);
				for(int i=0; i<lottoNumber.size(); i++) {
					array[i].setText("     " + lottoNumber.get(i));
				}
				lotto = lottoNumber.toString();
			}
		});


		this.setLayout(null);
		spendMoneyButton = new JButton("���� �Һ�ݾ�");
		spendMoneyButton.setBounds(150,300, 200, 30);
		this.add(spendMoneyButton);
		spendMoneyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!lottoNumberMakeUsed) {
					JOptionPane.showMessageDialog(LottoNumberMaker.this, "��ȣ�� ���� ���� �����ּ���");
					return;
				}
				expectedMoneyUsed = true;
				long money = 1000L;
				long sum = 0L;
				Set<Long> expectedMoneySet = new HashSet<Long>();
				while(true) {
					sum += money;
					expectedMoneySet.removeAll(expectedMoneySet);
					for(int i=0; expectedMoneySet.size()<6; i++) {
						expectedMoneySet.add((long)(Math.random()*45+1));
					}
					List<Long> expectedMoney = new LinkedList<Long>(expectedMoneySet);
					Collections.sort(expectedMoney);

					if(expectedMoney.equals(lottoNumber)) {
						sumResult = df.format(sum);
						moneyResultLable.setText(sumResult);
						lottoNumberMakeUsed = false;
						break;
					}
				}
			}
		});

	}

}