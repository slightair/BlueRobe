using UnityEngine;

public class TitleSceneController : MonoBehaviour
{

    void Start()
    {

    }

    void Update()
    {

    }

    public void OnTapToStartButton()
    {
        StartGame();
    }

    private void StartGame()
    {
        Application.LoadLevel("DebugStage");
    }
}