import web
from pprint import pprint
import pickle
import os.path

urls = (
    '/register' ,'register',
    '/unregister','unregister'
)

regFile = "device_ids.reg"

class register:
   def GET(self):
	return "register with POST to this url"
   def POST(self):
	data = web.input()
	# read data from file if exists
	if os.path.isfile(regFile):
		with open(regFile, 'rb') as f:
			reg_list = pickle.load(f)
	else:
		reg_list = list()
	# add id to list
	if data.regId not in reg_list:
		reg_list.append(data.regId)
		# write file
		with open(regFile, 'wb') as f:
    			pickle.dump(reg_list, f)
	return "OK"

class unregister:
   def GET(self):
	return "unregister with POST to this url"
   def POST(self):
	data = web.input()	
        # read data from file if exists
        if os.path.isfile(regFile):
                with open(regFile, 'rb') as f:
                        reg_list = pickle.load(f)
		#write file
		if data.regId in reg_list:
                	reg_list.remove(data.regId)
	                # write file
        	        with open(regFile, 'wb') as f:
                	        pickle.dump(reg_list, f)	
	return "OK"

if __name__ == "__main__":
    app = web.application(urls, globals())
    app.run()
