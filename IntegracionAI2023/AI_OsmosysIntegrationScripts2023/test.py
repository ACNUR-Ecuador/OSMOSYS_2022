from activityinfo import FormProcessing
import osmosys.osmosys

print('Integration')
aiToken = osmosys.osmosys.getOsmosysConnection()
print(aiToken)
osmosysData = osmosys.osmosys.get_ai_form_level_1()
print(osmosysData.info())
