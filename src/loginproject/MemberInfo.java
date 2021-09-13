package loginproject;

public class MemberInfo {
	//iv
	private String id;
	private String passward;    
	private String name;
	private String email;
	private String mobile;
	private String subscribe;

	//const
	public MemberInfo() {
	}




	public MemberInfo(String id, String passward, String name, String email, String mobile) {
		super();
		this.id = id;
		this.passward = passward;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
	}
	//metohds
	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getPassward() {
		return passward;
	}

	public void setPassward(String passward) {
		this.passward = passward;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}