using UnityEngine;

public class MainCameraControl : MonoBehaviour
{
    public GameObject character;
    public Vector3 basePosition;

    // Use this for initialization
    void Start()
    {
        basePosition = transform.position;
    }

    // Update is called once per frame
    void Update()
    {
        Vector3 characterPosition = character.transform.position;
        Vector3 newPosition = new Vector3(basePosition.x, basePosition.y, basePosition.z);
        newPosition.x += characterPosition.x;
        newPosition.z += characterPosition.z;
        transform.position = newPosition;
    }
}
