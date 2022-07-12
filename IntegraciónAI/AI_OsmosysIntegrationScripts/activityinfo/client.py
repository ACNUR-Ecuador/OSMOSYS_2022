import requests
from activityinfo import auth
from requests.exceptions import HTTPError
import configparser



class Client:
    """Client to interact with the ActivityInfo API."""

    def __init__(self, token, base_url='https://www.activityinfo.org'):
        """Initialize a Client object

        :param token: Your API token.
        :param base_url: The base URL of the ActivityInfo API **without** a trailing backslash.
        """
        self.auth = auth.TokenAuth(token)
        self.base_url = base_url

    def get_resource(self, path, query_params=None):
        """Send a GET request to the ActivityInfo API

        :param path: The path of the resource. For example, 'resources/databases'.
        :param query_params: Dictionary, list of tuples or bytes to send in the query string for the request.
        :return: JSON-encoded contents of the response.
        """
        r = requests.get(url=self.base_url + '/' + path,
                         params=query_params,
                         auth=self.auth,
                         headers={'Accept': 'application/json'})
        r.raise_for_status()
        return r.json()

    def post_resource(self, path, body):
        """Send a POST request to the ActivityInfo API

        :param path: The path of the resource. For example, 'resources/databases'.
        :param body: JSON payload.
        :return: JSON-encoded contents of the response.
        """

        try:
            r = requests.post(url=self.base_url + '/' + path,
                              data=body,
                              auth=self.auth,
                              headers={'Content-type': 'application/json', 'Accept': 'application/json'})
            if r.status_code in [200]:
                return None
            r.raise_for_status()
            return r.json()
        except HTTPError as exc:
            code = exc.response.status_code

            if code in [200]:
                return
            raise

