Map = function(){
	 this.map = new Object();
	};   
	Map.prototype = {   
	    put : function(key, value){   
	        this.map[key] = value;
	    },   
	    get : function(key){   
	        return this.map[key];
	    },
	    containsKey : function(key){    
	     return key in this.map;
	    },
	    containsValue : function(value){    
	     for(var prop in this.map){
	      if(this.map[prop] == value) return true;
	     }
	     return false;
	    },
	    isEmpty : function(key){    
	     return (this.size() == 0);
	    },
	    clear : function(){   
	     for(var prop in this.map){
	      delete this.map[prop];
	     }
	    },
	    remove : function(key){    
	     delete this.map[key];
	    },
	    keys : function(){   
	        var keys = new Array();   
	        for(var prop in this.map){   
	            keys.push(prop);
	        }   
	        return keys;
	    },
	    values : function(){   
	     var values = new Array();   
	        for(var prop in this.map){   
	         values.push(this.map[prop]);
	        }   
	        return values;
	    },
	    size : function(){
	      var count = 0;
	      for (var prop in this.map) {
	        count++;
	      }
	      return count;
	    }
	};
	
	var map = new Map();
	 map.put("45|800000|KEMCTI2_DEVICE_SET_1_act", null);
	 map.put("46|800000|KEMCTI2_DEVICE_SET_2_act", null);
	 map.put("47|800000|KEMCTI2_DEVICE_SET_3_act", null);
	 map.put("48|800000|KEMCTI2_DEVICE_SET_4_act", null);
	 
	 map.put("45|800000|KEMCTI2_DEVICE_SET_1_alert", null);
	 map.put("46|800000|KEMCTI2_DEVICE_SET_2_alert", null);
	 map.put("47|800000|KEMCTI2_DEVICE_SET_3_alert", null);
	 map.put("48|800000|KEMCTI2_DEVICE_SET_4_alert", null);
	 
	 map.put("45|800000|KEMCTI2_DEVICE_SET_1_update", null);
	 map.put("46|800000|KEMCTI2_DEVICE_SET_2_update", null);
	 map.put("47|800000|KEMCTI2_DEVICE_SET_3_update", null);
	 map.put("48|800000|KEMCTI2_DEVICE_SET_4_update", null);
	 
	 map.put("45|800000|KEMCTI2_DEVICE_SET_1_period", null);
	 map.put("46|800000|KEMCTI2_DEVICE_SET_2_period", null);
	 map.put("47|800000|KEMCTI2_DEVICE_SET_3_period", null);
	 map.put("48|800000|KEMCTI2_DEVICE_SET_4_period", null);