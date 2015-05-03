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
    }
}