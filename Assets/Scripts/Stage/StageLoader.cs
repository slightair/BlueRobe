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

            ChangePlaneColor();
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

        private void ChangePlaneColor()
        {
            GameObject plane = GameObject.Find("Plane");
            Renderer renderer = plane.GetComponent("Renderer") as Renderer;

            Color color;
            switch (map.groundType)
            {
                case Map.GroundType.Grass:
                    color = new Color(0.6f, 1.0f, 0.3f, 1.0f);
                    break;
                default:
                    color = new Color(0.7f, 0.7f, 0.7f, 1.0f);
                    break;
            }
            renderer.material.color = color;
        }

        private void ArrangeObjects()
        {
            for (int y = 0; y < map.height; y++)
            {
                for (int x = 0; x < map.width; x++)
                {
                    Tile tile;
                    tile = map.GetTile("Items", x, y);
                    if (tile != null)
                    {
                        GameObject itemPrefab = (GameObject)Resources.Load("Objects/" + tile.name);
                        Vector3 position = new Vector3(1 - y * 2, 0, -10 + x * 2);
                        Instantiate(itemPrefab, position, Quaternion.identity);
                    }

                    tile = map.GetTile("Objects", x, y);
                    if (tile != null)
                    {
                        GameObject objectPrefab = (GameObject)Resources.Load("Objects/" + tile.name);
                        Vector3 position = new Vector3(1 - y * 2, 0, -10 + x * 2);
                        Instantiate(objectPrefab, position, Quaternion.identity);
                    }

                    tile = map.GetTile("Obstacles", x, y);
                    if (tile != null)
                    {
                        GameObject obstaclePrefab = (GameObject)Resources.Load("Objects/" + tile.name);
                        Vector3 position = new Vector3(1 - y * 2, 0, -10 + x * 2);
                        Instantiate(obstaclePrefab, position, Quaternion.identity);
                    }
                }
            }
        }
    }
}