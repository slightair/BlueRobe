using UnityEngine;

namespace BlueRobe.Scene
{
    public class ActionStageSceneController : MonoBehaviour
    {
        void Start()
        {

        }

        void Update()
        {

        }

        public void OnStageClear()
        {

        }

        public void OnGameOver()
        {
            SceneManager.Instance.SwitchScene("Title");
        }
    }
}
