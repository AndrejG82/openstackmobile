from kombu import Connection, Exchange, Queue
from pprint import pprint
from gcm import GCM
import pickle
import os.path
import time

nova_x = Exchange('nova', type='topic', durable=False)
info_q = Queue('notifications.info', exchange=nova_x, durable=False, 
               routing_key='notifications.info')

gcmEvents = ['compute.instance.pause.end','compute.instance.unpause.end',
             'compute.instance.power_off.end','compute.instance.power_on.end',
    	     'compute.instance.suspend','compute.instance.resume']

gcmEventsMessage = ['Instance has been paused.','Instance has been resumed.',
                    'Instance has been powered off.','Instance has been powered on.',
	            'Instance has been suspended.','Instance has been resumed.']	

# put your key here instead of XXX				
gcm = GCM("XXX")
# read ids from file
regFile = "device_ids.reg"
if os.path.isfile(regFile):
	with open(regFile, 'rb') as f:
		reg_ids = pickle.load(f)
else:
	reg_ids = []

def gcm_send(body):
    eventType = body['event_type']
    project = body['_context_project_name']
    computerName = body['payload']['display_name']

    messageHead = project + ' - ' + computerName 
    messageBody = gcmEventsMessage[gcmEvents.index(eventType)] + ' (' + time.strftime("%d.%m.%y %H:%M:%S") + ')'

    # send to GCM
    data = {'messageHead': messageHead, 'messageBody': messageBody}
    print 'broadcasting: ' +  messageHead + ',' + messageBody
    response = gcm.json_request(registration_ids=reg_ids, data=data)



def process_msg(body, message):
    # check for certain types of events
    eventType = body['event_type']
    if eventType in gcmEvents:
		gcm_send(body)
    message.ack()

with Connection('amqp://guest:stack@localhost//') as conn:
    with conn.Consumer(info_q, callbacks=[process_msg]):
        while True:
            try:
                conn.drain_events()
            except KeyboardInterrupt:
                break

