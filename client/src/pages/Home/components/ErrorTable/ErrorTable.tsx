import { useState } from "react";
import { Pagination } from "antd";

type DataType = {
  errorReason: string;
  status: string;
  client: string;
  dateTime: string;
  aiModels: string;
};

type ErrorTableProps = {
  data: DataType[];
};

export default function ErrorTable({ data }: ErrorTableProps) {
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 5;

  const startIndex = (currentPage - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  const currentData = data.slice(startIndex, endIndex);

  return (
    <div className="w-full flex flex-col items-center">
      <div className="bg-white shadow-md rounded-md overflow-x-auto w-full max-w-4xl">
        <table className="min-w-full text-center">
          <thead className="bg-[#F31E8D]">
            <tr>
              <th className="px-6 py-3 font-semibold text-white">
                Mensagem do Erro
              </th>
              <th className="px-6 py-3 font-semibold text-white">Status</th>
              <th className="px-6 py-3 font-semibold text-white">Cliente</th>
              <th className="px-6 py-3 font-semibold text-white">
                Data e Hora
              </th>
              <th className="px-6 py-3 font-semibold text-white">
                Modelos de IA
              </th>
            </tr>
          </thead>
          <tbody>
            {currentData.map((row, idx) => (
              <tr
                key={idx}
                className={idx % 2 === 0 ? "bg-white" : "bg-gray-100"}
              >
                <td className="px-6 py-4">{row.errorReason}</td>
                <td className="px-6 py-4">{row.status}</td>
                <td className="px-6 py-4">{row.client}</td>
                <td className="px-6 py-4">{row.dateTime}</td>
                <td className="px-6 py-4">{row.aiModels}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="w-full max-w-4xl mt-4 px-4">
        <div className="bg-white rounded-md shadow-md p-4 flex justify-center">
          <Pagination
            current={currentPage}
            total={data.length}
            pageSize={pageSize}
            showTotal={(total) =>
              `Exibindo ${currentData.length} relatórios do total de ${total} relatórios`
            }
            onChange={(page) => setCurrentPage(page)}
            responsive
            showSizeChanger={false}
          />
        </div>
      </div>
    </div>
  );
}
