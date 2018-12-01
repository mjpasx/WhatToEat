
public class EntityClass {
	
	public String mealName;
	public double sentimentScore;
	public String review;
	public String restaurantName;
	public String zipcode;
	public String user;
	public String timestamp;
	public String description;
	
	public EntityClass()
	{
		
	}
	
	public EntityClass(String w, double sen)
	{
		this.mealName = w;
		this.sentimentScore = sen;
	}
	
	public EntityClass(String w, double sen, String rev, String rest, String code, String name, String t, String desc)
	{
		this.mealName = w;
		this.sentimentScore = sen;
		this.review = rev;
		this.restaurantName = rest;
		this.zipcode = code;
		this.user = name;
		this.timestamp = t;
		this.description = desc;
	}
	
	public void SetWord(String w)
	{
		this.mealName = w;
	}
	
	public void SetSentiment(double sen)
	{
		this.sentimentScore = sen;
	}
	
	public void SetReview(String rev)
	{
		this.review = rev;
	}
	
	public void SetRestName(String name)
	{
		this.restaurantName = name;
	}
	
	public void SetZipCode(String code)
	{
		this.zipcode = code;
	}
	
	public void SetName(String name)
	{
		this.user = name;
	}
	
	public void SetTime(String t)
	{
		this.timestamp = t;
	}
	
	public void SetDescription(String desc)
	{
		this.description = desc;
	}
	
	public String GetWord()
	{
		return this.mealName;
	}
	
	public double GetSentiment()
	{
		return this.sentimentScore;
	}
	
	public String GetReview()
	{
		return this.review;
	}
	
	public String GetRestName()
	{
		return this.restaurantName;
	}
	
	public String GetZipCode()
	{
		return this.zipcode;
	}
	
	public String GetUser()
	{
		return this.user;
	}
	
	public String GetTime()
	{
		return this.timestamp;
	}
	
	public String GetDescription()
	{
		return this.description;
	}

}
