
public class ReviewClass {
	private String analyzedReview;
	private String review;
	private String restName;
	private String zipCode;
	private String user;
	private String time;
	private String description;
	
	public ReviewClass()
	{
		
	}
	
	public ReviewClass(String rev, String rest, String code, String name, String t)
	{
		this.review = rev;
		this.restName = rest;
		this.zipCode = code;
		this.user = name;
		this.time = t;
	}
	
	public void SetAnalyzedReview(String analyzed)
	{
		this.analyzedReview = analyzed;
	}
	
	public void SetReview(String rev)
	{
		this.review = rev;
	}
	
	public void SetRestName(String name)
	{
		this.restName = name;
	}
	
	public void SetZipCode(String code)
	{
		this.zipCode = code;
	}
	
	public void SetName(String name)
	{
		this.user = name;
	}
	
	public void SetTime(String t)
	{
		this.time = t;
	}
	
	public void SetDescription(String desc)
	{
		this.description = desc;
	}
	
	public String GetAnalyzedReview()
	{
		return this.analyzedReview;
	}
	
	public String GetReview()
	{
		return this.review;
	}
	
	public String GetRestName()
	{
		return this.restName;
	}
	
	public String GetZipCode()
	{
		return this.zipCode;
	}
	
	public String GetUser()
	{
		return this.user;
	}
	
	public String GetTime()
	{
		return this.time;
	}
	
	public String GetDescription()
	{
		return this.description;
	}
}
