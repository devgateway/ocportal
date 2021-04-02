from github import Github
import requests
import json
import os
REPO_STR = "devgateway/forms-makueni"
PERSONAL_TOKEN = "[token]"
SLACK_URL = "[webookurl_fromslack]"
PR_URL = "https://github.com/devgateway/forms-makueni/pull/"
PREV_SLACK_ALERT_FILE = "prev_slack_alert.json"
g = Github(PERSONAL_TOKEN)
repo = g.get_repo(REPO_STR)
pulls = repo.get_pulls(state='open')
text = "The following Pull Requests have conflicts:\n"
has_dirty = False
def same_previous_slack_alert(current_alert):
    if not os.path.exists(PREV_SLACK_ALERT_FILE):
        return False
    f = open(PREV_SLACK_ALERT_FILE, "r")
    ret = f.read() == current_alert
    f.close()
    return ret
def write_slack_alert(alert_str):
    f = open(PREV_SLACK_ALERT_FILE, "w+")
    f.write(alert_str)
    f.close()
for pr in pulls:
    if pr.mergeable_state == 'dirty':
        has_dirty = True
        text += '- <' + PR_URL + str(pr.number) + "|" + pr.title + ">\n"
request = {'text': text}
request_str = json.dumps(request)
if has_dirty and not same_previous_slack_alert(request_str):
    write_slack_alert(request_str)
    requests.post(url=SLACK_URL, json=request)