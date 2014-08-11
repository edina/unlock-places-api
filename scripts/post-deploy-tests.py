'''
File: post-deploy-tests.py
Author: Colin Gormley
Description:
This script should be run after performing a deployment or a release.
It is intended to check core functionality to ensure nothing serious has gone wrong

It should test a number of the Places API endpoints and check for and expected text snippet

'''
import argparse
import requests

class TestPlacesAPI():
    """docstring for TestPlacesAPI"""
    def __init__(self):
        """
        Init test queries and expected text snippets
        """

        self.data= [{'search1':{'expected':'14131223', 'url':'/search?name=edinburgh'}},
            {'search2':{'expected':'10108', 'url':'/search?name=irvine'}},
            {'search3':{'expected':'-3.0108649760249175, 55.872636253841826, -3.3543721791120378, 55.99322049797085', 'url':'/search?name=Edinburgh,Glasgow&gazetteer=os,%20geonames&format=json'}},
            {'search4':{'expected':'-4.083416620353358,55.769992819541876,0 -4.083416620353358,55.956271871797036,0', 'url':'/search?name=Edinburgh,Glasgow&featureType=cities&gazetteer=os&format=kml'}},
            {'search5':{'expected':'Edinburgh', 'url':'/search?name=Edinburgh&featureType=farm&gazetteer=geonames&format=georss'}},
            {'search6':{'expected':'"-3.1793831471035228", "55.93650827103622", "-3.1793831471035228", "55.93650827103622"', 'url':'/search?name=EH91PR&format=txt'}},
            {'search7':{'expected':'-3.240267949416604,55.921370013677084', 'url':'/search?name=Craiglockhart&minx=-3.35081&miny=55.87272&maxx=-2.01274&maxy=55.99475&operator=within&gazetteer=os'}},
            {'search8':{'expected':'2641942', 'url':'/search?name=Portobello,Musselburgh&minx=-3.35081&miny=55.87272&maxx=-2.01274&maxy=55.99475&format=json&operator=within&gazetteer=geonames'}},
            {'search9':{'expected':'-3.240267949416604,55.921370013677084,0 -3.240267949416604,55.921370013677084,0 -3.240267949416604,55.921370013677084,0 -3.240267949416604,55.921370013677084,0 -3.240267949416604,55.921370013677084,0', 'url':'/search?name=Craiglockhart&minx=-3.35081&miny=55.87272&maxx=-3.01274&maxy=55.99475&operator=intersect&format=kml&gazetteer=os'}},
            {'search10':{'expected':'"-3.9903153778615335", "56.19015132770522", "-3.9903153778615335", "56.19015132770522"', 'url':'/search?name=stockbridge&format=txt&gazetteer=open&country=UK'}},
            {'nameSearch':{'expected':'-3.5680713653564453', 'url':'/nameSearch?name=Edinburgh'}},
            {'nameSearch2':{'expected':'-3.0108649760249175, 55.872636253841826, -3.3543721791120378, 55.99322049797085', 'url':'/nameSearch?name=Edinburgh,Glasgow&gazetteer=os,%20geonames&format=json'}},
            {'nameAndFeatureSearch':{'expected':'-3.0108649760249175,55.872636253841826,0 -3.0108649760249175,55.99322049797085,0', 'url':'/nameAndFeatureSearch?name=Edinburgh,Glasgow&featureType=cities&gazetteer=os&format=kml'}},
            {'uniqueNameSearch':{'expected':'-3.1823691373333958,55.93292837590634', 'url':'/uniqueNameSearch?name=Edinburgh&gazetteer=os&format=xml'}},
            {'closestMatchSearch':{'expected':'435791', 'url':'/closestMatchSearch?name=Edinburgh&gazetteer=geonames&format=json'}},
            {'footprintLookup1':{'expected':'55.9762017236874 -3.3017230249143 55.9761206947946 -3.30173632123675 55.9755454412297', 'url':'/footprintLookup?identifier=9656&gazetteer=os&format=georss'}},
            {'footprintLookup2':{'expected':'FootprintCollection', 'url':'/footprintLookup?identifier=5823266,5823268&gazetteer=geonames&format=json'}},
            {'footprintLookup3':{'expected':'55.9762017236874 -3.3017230249143 55.9761206947946', 'url':'/footprintLookup?identifier=9656&gazetteer=os&format=georss'}},
            {'footprintLookup4':{'expected':'-3.3017230249143, 55.9762017236874', 'url':'/footprintLookup?identifier=5823266,5823268,9656&gazetteer=geonames&format=json'}},
            {'footprintLookup5':{'expected':'55.9762017236874 -3.3017230249143 55.9761206947946 -3.30173632123675', 'url':'/footprintLookup?identifier=9656&gazetteer=os&format=georss'}},
            {'supportedFeatureTypes':{'expected':'EDINA.G.B.E', 'url':'/supportedFeatureTypes?&gazetteer=os,geonames&format=xml'}},
            {'featureLookup':{'expected':'"-3.3543721791120378", "55.872636253841826", "-3.0108649760249175", "55.99322049797085"', 'url':'/featureLookup?id=9656&gazetteer=os&format=txt'}},
            {'featureLookup2':{'expected':'-1.4715618655560048', 'url':'/featureLookup?format=html&identifier=9692'}},
            {'featureLookup3':{'expected':'"-3.3543721791120378", "55.872636253841826"', 'url':'/featureLookup?id=9656&gazetteer=os&format=txt'}},
            {'postcodeSearch':{'expected':'"-3.1793831471035228", "55.93650827103622"', 'url':'/postCodeSearch?postCode=EH91PR&gazetteer=unlock&format=txt'}},
            {'spatialNameSearch':{'expected':'-3.240267949416604,55.921370013677084', 'url':'/spatialNameSearch?name=Craiglockhart&minx=-3.35081720352173&miny=55.87272644042972&maxx=-2.01274991035461&maxy=55.9947509765625&format=xml&operator=within&gazetteer=os'}},
            {'spatialNameSearch2':{'expected':'population', 'url':'/spatialNameSearch?name=Portobello,Musselburgh&minx=-3.35081720352173&miny=55.87272644042972&maxx=-2.01274991035461&maxy=55.9947509765625&format=json&operator=within&gazetteer=os,geonames'}},
            {'spatialFeatureSearch':{'expected':'55.9508 -3.18738', 'url':'/spatialFeatureSearch?featureType=hotel&minx=-3.35081720352173&miny=55.87272644042972&maxx=-3.01274991035461&maxy=55.9947509765625&operator=within&format=georss&gazetteer=geonames'}},
            {'deepSearch':{'expected':'Digital Exposure of English Place-names (DEEP)', 'url':'/search?name=edinburgh&searchVariants=false&gazetteer=deep'}},
            {'deepSearch2':{'expected':'Abbandun', 'url':'/search?minx=-1.7&miny=51.0&maxx=-1.0&maxy=52.0&operator=within&gazetteer=deep&featureType=Sub-Parish&searchVariants=true'}},
            {'deepSearch3':{'expected':'-1.63278040413889,53.24867212509395', 'url':'/search?name=Bubbynhyll&deepSrc=IpmR&searchVariants=true&gazetteer=deep'}},
            {'deepSearch4':{'expected':'Alfordes howe', 'url':'/search?gazetteer=deep&featureType=Hundred&searchVariants=true&startYear=1485&endYear=1689'}},
            {'deepAttestationSearch':{'expected':'epns-deep-27-b-name-w41007', 'url':'/search?variantId=epns-deep-27-b-name-w41007&gazetteer=deep&format=xml'}},
            {'closestMatchSearch':{'expected':'435791', 'url':'/closestMatchSearch?name=Edinburgh&gazetteer=geonames&format=json'}}
        ]
        self.verbose = False

    def _parse_args(self):
        """Parse command line arguments then run queries"""

        parser = argparse.ArgumentParser(description="""Script to sanity check places api:
        """)
        parser.add_argument('-url', action='store',
                            dest='url',
                            default='local',
                            help='Url to run test against: [local|unlock]')
        parser.add_argument('-verbose', action='store_true', default=False, dest='verbose', help='Show query responses')
        results = parser.parse_args()
        url = results.url
        self.verbose = results.verbose
        if url == 'local':
            url = 'http://localhost:8080/unlock-api/ws'
        elif url == 'unlock':
            url = 'http://unlock.edina.ac.uk/ws'

        print 'Using %s' % url
        self.run_queries(url)

    def run_queries(self, url):
        """Runs each query against the specified url """

        for item in self.data:
            key = item.keys()[0]
            for i in item.values():
                expected_text =  i['expected']
                query =  i['url']
                target = '%s%s' %(url, query)
                print 'Running test %s for %s' % (key, target)
                r = requests.get(target)
                if self.verbose:
                    print r.text
                assert r.status_code == requests.codes.ok
                assert expected_text in r.text

        print 'Done'

if __name__ == '__main__':
    test = TestPlacesAPI()
    test._parse_args()
