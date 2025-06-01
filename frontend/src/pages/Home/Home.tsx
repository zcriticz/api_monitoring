import {useState} from "react";
import {Layout, Menu, DatePicker, Input} from "antd";
import {
    BarChartOutlined,
    TableOutlined,
} from "@ant-design/icons";
import HeaderComponent from "@/pages/Home/components/Header";
import DownloadButton from "@/pages/Home/components/DownloadButton";
import ErrorTable from "@/pages/Home/components/ErrorTable";
import DashBoard from "@/pages/Home/components/Dashboard";

const {Content, Sider} = Layout;
const {Search} = Input;

export default function App() {
    const [dashboard, setDashboard] = useState(true);

    return (
        <Layout style={{minHeight: "100vh"}}>
            <Sider
                width={220}
                style={{
                    backgroundColor: "#F31E8D",
                    position: "fixed",
                    height: "100vh",
                    left: 0,
                    top: 0,
                    bottom: 0,
                    zIndex: 1000,
                }}
                className="text-white"
            >
                <div className="text-white text-xl font-bold py-4 text-center">
                    ExtrAI Dados
                </div>
                <Menu
                    theme="dark"
                    mode="inline"
                    selectedKeys={[dashboard ? "1" : "2"]}
                    onClick={({key}) => setDashboard(key === "1")}
                    style={{backgroundColor: "#F31E8D", borderRight: 0}}
                    items={[
                        {
                            key: "1",
                            icon: <BarChartOutlined/>,
                            label: "Dashboard",
                        },
                        {
                            key: "2",
                            icon: <TableOutlined/>,
                            label: "Tabela de Erros",
                        },
                    ]}
                />
            </Sider>

            <Layout style={{marginLeft: 220}}>
                <div className="bg-white shadow-md sticky top-0 z-10">
                    <HeaderComponent/>
                </div>

                <Content className="px-4 sm:px-8 py-6 bg-white">
                    <h1 className="text-2xl font-bold text-gray-800 ml-2 mb-1">
                        Relatórios do ExtrAI Dados
                    </h1>
                    <hr className="h-[3px] bg-[#F31E8D] w-full mb-4 border-none max-w-4xl"/>

                    <div
                        className="w-full max-w-6xl flex flex-wrap justify-center sm:justify-start items-center gap-4 mb-6">
                        <Search
                            placeholder="Pesquisar..."
                            allowClear
                            onSearch={(value) => console.log(value)}
                            className="!w-full sm:!w-72"
                        />
                        <DatePicker
                            className="!w-full sm:!w-48 !border-pink-500 !rounded-md focus:!border-pink-500 hover:!border-pink-500 focus:!ring-pink-300"/>
                        <div className="w-full sm:w-auto flex justify-center sm:justify-start">
                            <DownloadButton/>
                        </div>
                    </div>

                    {dashboard ? <DashBoard/> : <ErrorTable/>}
                </Content>
            </Layout>
        </Layout>
    );
}
