using UnityEngine;
using BlueRobe.Utility;

namespace BlueRobe.Scene
{
    public class SceneManager : SingletonMonoBehaviour<SceneManager>
    {
        public void SwitchScene(string sceneName)
        {
            Application.LoadLevel(sceneName);
        }
    }
}