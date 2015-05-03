using UnityEngine;
using System.IO;
using System.Xml;

namespace BlueRobe.Stage
{
    public class StageLoader : MonoBehaviour
    {
        private Map map;

        // Use this for initialization
        void Start()
        {
            map = LoadMap("debug");

            ArrangeObjects();
        }

        // Update is called once per frame
        void Update()
        {
        }

        private Map LoadMap(string stageName)
        {
            TextAsset stageXmlAsset = Resources.Load<TextAsset>("Stages/" + stageName + ".tmx");

            XmlDocument document = new XmlDocument();
            document.Load(new StringReader(stageXmlAsset.text));

            return new Map(document.DocumentElement);
        }

        private void ArrangeObjects()
        {
            for (int y = 0; y < map.height; y++)
            {
                for (int x = 0; x < map.width; x++)
                {
                    Tile tile = map.GetTile("Items", x, y);
                    if (tile != null)
                    {
                        GameObject itemPrefab = (GameObject)Resources.Load("Objects/" + tile.name);
                        Vector3 position = new Vector3(1 - y * 2, 0, -10 + x * 2);
                        Instantiate(itemPrefab, position, Quaternion.identity);
                    }
                }
            }
        }
    }
}