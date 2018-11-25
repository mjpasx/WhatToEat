
public class RestaurantClass {

	private String restName;
	private String businessId;
	private String zipCode;
	
	public RestaurantClass()
	{
		this.restName = null;
		this.businessId = null;
		this.zipCode = null;
	}
	
	public RestaurantClass(String name, String id, String code)
	{
		this.restName = name;
		this.businessId = id;
		this.zipCode = code;
	}
	
	public void SetRestName(String name)
	{
		this.restName = name;
	}
	
	public void SetId(String id)
	{
		this.businessId = id;
	}
	
	public void SetZipCode(String code)
	{
		this.zipCode = code;
	}
	
	
	public String GetRestName()
	{
		return this.restName;
	}
	
	public String GetId()
	{
		return this.businessId;
	}
	
	public String GetZipCode()
	{
		return this.zipCode;
	}
	
}
