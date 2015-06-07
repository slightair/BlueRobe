using UnityEngine;
using System.IO;
using System.Xml;

namespace BlueRobe.Stage
{
    public class ActionStageLoader : MonoBehaviour
    {
        private Map map;

        // Use this for initialization
        void Start()
        {
            map = LoadMap("debug");

            ChangePlaneColorAndSize();
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

        private void ChangePlaneColorAndSize()
        {
            GameObject plane = GameObject.Find("Plane");
            GameObject stopper = GameObject.Find("Stopper");
            Renderer planeRenderer = plane.GetComponent("Renderer") as Renderer;

            Color color;
            switch (map.groundType)
            {
                case Map.GroundType.Grass:
                    color = new Color(0.57f, 0.87f, 0.26f, 1.0f);
                    break;
                default:
                    color = new Color(0.7f, 0.7f, 0.7f, 1.0f);
                    break;
            }
            planeRenderer.material.color = color;

            float planeMargin = 10 * 2;
            float planeDepth = map.height * 2 + planeMargin * 3;

            plane.transform.position = new Vector3(-planeDepth / 2 + planeMargin, -1f, 0f);
            plane.transform.localScale = new Vector3(planeDepth, 2f, 34f);

            stopper.transform.position = new Vector3(-planeDepth + planeMargin, 10f, 0f);
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
                        GameObject itemPrefab = (GameObject)Resources.Load("Items/" + tile.name);
                        Vector3 position = new Vector3(-1 - y * 2, 0, -10 + x * 2);
                        Instantiate(itemPrefab, position, Quaternion.identity);
                    }

                    tile = map.GetTile("Objects", x, y);
                    if (tile != null)
                    {
                        GameObject objectPrefab = (GameObject)Resources.Load("Objects/" + tile.name);
                        Vector3 position = new Vector3(-1 - y * 2, 0, -10 + x * 2);
                        Instantiate(objectPrefab, position, Quaternion.identity);
                    }

                    tile = map.GetTile("Obstacles", x, y);
                    if (tile != null)
                    {
                        GameObject obstaclePrefab = (GameObject)Resources.Load("Obstacles/" + tile.name);
                        Vector3 position = new Vector3(-1 - y * 2, 0, -10 + x * 2);
                        Instantiate(obstaclePrefab, position, Quaternion.identity);
                    }
                }
            }
        }
    }
}