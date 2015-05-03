using System;
using System.Xml;
using System.Linq;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;

namespace BlueRobe.Stage
{
    public class Layer
    {
        public string name;
        public int width;
        public int height;
        public List<UInt32> tileIdList;

        public Layer(XmlNode node)
        {
            name = node.Attributes["name"].Value;
            width = int.Parse(node.Attributes["width"].Value);
            height = int.Parse(node.Attributes["height"].Value);

            ParseData(node);
        }

        private void ParseData(XmlNode node)
        {
            string dataString = node.SelectSingleNode("data").InnerText;
            MemoryStream decodedDataStream = new MemoryStream(Convert.FromBase64String(dataString));

            // drop 2 bytes - http://blogs.msdn.com/b/bclteam/archive/2007/05/16/system-io-compression-capabilities-kim-hamilton.aspx
            decodedDataStream.ReadByte();
            decodedDataStream.ReadByte();

            DeflateStream decompStream = new DeflateStream(decodedDataStream, CompressionMode.Decompress);
            BinaryReader reader = new BinaryReader(decompStream);

            tileIdList = new List<UInt32>();
            for (int i = 0; i < width * height; i++)
            {
                tileIdList.Add(reader.ReadUInt32());
            }
            tileIdList.AsReadOnly();
        }

        override public string ToString()
        {
            StringWriter writer = new StringWriter();
            for (int y = 0; y < height; y++)
            {
                List<UInt32> lines = tileIdList.GetRange(y * width, width);
                writer.WriteLine(string.Join("", lines.Select(i => i.ToString()).ToArray()));
            }
            return writer.ToString();
        }
    }
}